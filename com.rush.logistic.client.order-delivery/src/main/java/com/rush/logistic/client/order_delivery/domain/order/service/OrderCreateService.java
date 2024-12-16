package com.rush.logistic.client.order_delivery.domain.order.service;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery.repository.DeliveryRepository;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.delivery_route.repository.DeliveryRouteRepository;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliverymanInChargeTypeEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliverymanStatusEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.exception.DeliverymanCode;
import com.rush.logistic.client.order_delivery.domain.deliveryman.exception.DeliverymanException;
import com.rush.logistic.client.order_delivery.domain.deliveryman.repository.DeliverymanRepository;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.CompanyClient;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.HubClient;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.SlackClient;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.request.GetStartEndHubIdOfCompanyReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.request.SendSlackMessageReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.*;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAndDeliveryCreateReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderAllRes;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import com.rush.logistic.client.order_delivery.domain.order.repository.OrderRepository;
import com.rush.logistic.client.order_delivery.domain.order.service.model.HubRouteModel;
import com.rush.logistic.client.order_delivery.global.util.gemini.GeminiUtil;
import com.rush.logistic.client.order_delivery.global.util.navermap.NaverMapRes;
import com.rush.logistic.client.order_delivery.global.util.navermap.NaverMapUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderCreateService {
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliverymanRepository deliverymanRepository;
    private final DeliveryRouteRepository deliveryRouteRepository;

    private final CompanyClient companyClient;
    private final HubClient hubClient;
    private final SlackClient slackClient;

    private final NaverMapUtil naverMapUtil;
    private final GeminiUtil geminiUtil;

    @Transactional
    public OrderAllRes createDeliveryAndOrder(OrderAndDeliveryCreateReq requestDto, String userEmail) {
        // 수령, 생산 업체가 소속된 허브ID 받아옴
        CompanyResWrapper<GetStartEndHubIdOfCompanyRes> getStartEndHubIdOfCompanyDto = companyClient.getStartEndHubIdOfCompany(GetStartEndHubIdOfCompanyReq.toDto(requestDto.produceCompanyId(), requestDto.receiveCompanyId()));
        // TODO : -> 조회 실패할 경우 예외응답


        // 주문, 배송 생성
        Delivery delivery = requestDto.deliveryInfo().toEntity(getStartEndHubIdOfCompanyDto);
        Delivery deliverySaved = deliveryRepository.save(delivery);

        Order order = requestDto.toEntity(deliverySaved);
        Order orderSaved = orderRepository.save(order);

        // 허브 경로 받아옴
//        List<HubRouteModel> hubRouteModels = tempFeignClientGetRoutes(getStartEndHubIdOfCompanyDto);
//        log.info("OrderService createDeliveryAndOrder : hubRouteModels[0] : {}", hubRouteModels.get(0));
        HubResWrapper<HubRouteInfoRes> hubRouteInfoResWrap = hubClient.getHubRoute(
                getStartEndHubIdOfCompanyDto.result().departureHubId(), getStartEndHubIdOfCompanyDto.result().arrivalHubId());
        log.info("OrderCreateService createDeliveryAndOrder : hubRouteInfoResWrap : {}", hubRouteInfoResWrap);
        List<HubRouteInfoRes> hubRouteInfos = List.of(hubRouteInfoResWrap.data()); // TODO : 목록으로 바뀌면 변경하기
        List<HubRouteModel> hubRouteModels = HubRouteModel.fromDtos(hubRouteInfos);

        // 마지막 허브-업체 간 경로 받아옴
        NaverMapRes naverMapRes = naverMapUtil.getDistanceAndTimeByAddress(hubRouteInfoResWrap.data().endHubAddress(), requestDto.deliveryInfo().address());

        // 경로별 담당자 배정, 배송 경로 생성
        List<Deliveryman> assignedDeliverymans = new ArrayList<>();
        List<DeliveryRoute> deliveryRoutes = new ArrayList<>();
        assignHubDeliveryman(hubRouteModels, delivery, deliveryRoutes, assignedDeliverymans);
        assignCompanyDeliveryman(hubRouteModels.size(), naverMapRes, getStartEndHubIdOfCompanyDto.result().arrivalHubId(), delivery, deliveryRoutes, assignedDeliverymans);

        // 배송 담당자 영속화
        deliverymanRepository.saveAllAndFlush(assignedDeliverymans);

        // 배송 경로 영속화
        List<DeliveryRoute> savedDeliveryRoutes = deliveryRouteRepository.saveAllAndFlush(deliveryRoutes);
        log.info("OrderCreateService createDeliveryAndOrder : savedDeliveryRoutes[0] : {}", savedDeliveryRoutes.get(0));

        // 슬랙 알림
        alertExpectedTimeSlackMessage(userEmail, requestDto, hubRouteInfoResWrap.data(), orderSaved.getId());

        return OrderAllRes.fromEntity(orderSaved, savedDeliveryRoutes);
    }



    /**
     * 마지막 허브 - 업체 간 업체 배송 담당자 배정 메서드
     * @param sequence
     * @param hubToCompanyRoute
     * @param endHubId
     * @param delivery
     * @param deliveryRoutes
     * @param assignedDeliverymans
     */
    private void assignCompanyDeliveryman(Integer sequence, NaverMapRes hubToCompanyRoute, UUID endHubId, Delivery delivery, List<DeliveryRoute> deliveryRoutes, List<Deliveryman> assignedDeliverymans) {

        // 배송 담당자 배정
        Deliveryman deliveryman = null;
        Optional<Deliveryman> deliverymanOp = deliverymanRepository
                .findTop1ByLastHubIdAndInChargeTypeAndStatusOrderByLastDeliveryTimeAsc(endHubId, DeliverymanInChargeTypeEnum.COMPANY, DeliverymanStatusEnum.IDLE);
        if (deliverymanOp.isPresent()) {
            deliveryman = deliverymanOp.get();
            log.info("OrderCreateService assignHubDeliveryman : deliverymanOp.isPresent() : deliveryman : {}", deliveryman);
        }
        else {
            deliveryman = deliverymanRepository.findTop1ByHubInChargeIdAndInChargeTypeAndStatusOrderByLastDeliveryTimeAsc(endHubId, DeliverymanInChargeTypeEnum.COMPANY, DeliverymanStatusEnum.IDLE)
                    .orElseThrow(() -> new DeliverymanException(DeliverymanCode.AVAILABLE_DELIVERYMAN_NOT_EXIST));
            log.info("OrderCreateService assignHubDeliveryman : !deliverymanOp.isPresent() : deliveryman : {}", deliveryman);
        }

        // 배송 경로 생성
        DeliveryRoute deliveryRoute = hubToCompanyRoute.toDeliveryRouteEntity(sequence, endHubId, delivery, deliveryman);
        deliveryRoutes.add(deliveryRoute);
        log.info("OrderCreateService assignCompanyDeliveryman : deliveryRoute : {}", deliveryRoute);

        // 베송 담당자 상태 ASSIGNED 로 업데이트
        deliveryman.updateAssigned(deliveryRoute.getEndHubId(), deliveryRoute.getSequence());
        assignedDeliverymans.add(deliveryman);
    }

    /**
     * 허브 경로별 허브 배송 담당자 배정 메서드
     * @param hubRouteModels
     * @param delivery
     * @param deliveryRoutes
     * @param assignedDeliverymans
     */
    private void assignHubDeliveryman(List<HubRouteModel> hubRouteModels, Delivery delivery, List<DeliveryRoute> deliveryRoutes, List<Deliveryman> assignedDeliverymans) {
        for (HubRouteModel hubRouteModel : hubRouteModels) {
            UUID startHubId = hubRouteModel.startHubId();

            // 배송 담당자 배정
            Deliveryman deliveryman = null;
            Optional<Deliveryman> deliverymanOp = deliverymanRepository
                    .findTop1ByLastHubIdAndInChargeTypeAndStatusOrderByLastDeliveryTimeAsc(startHubId, DeliverymanInChargeTypeEnum.HUB, DeliverymanStatusEnum.IDLE);
            if (deliverymanOp.isPresent()) {
                deliveryman = deliverymanOp.get();
                log.info("OrderCreateService assignHubDeliveryman : deliverymanOp.isPresent() : deliveryman : {}", deliveryman);
            }
            else {
                deliveryman = deliverymanRepository.findTop1ByHubInChargeIdAndInChargeTypeAndStatusOrderByLastDeliveryTimeAsc(startHubId, DeliverymanInChargeTypeEnum.HUB, DeliverymanStatusEnum.IDLE)
                        .orElseThrow(() -> new DeliverymanException(DeliverymanCode.AVAILABLE_DELIVERYMAN_NOT_EXIST));
                log.info("OrderCreateService assignHubDeliveryman : !deliverymanOp.isPresent() : deliveryman : {}", deliveryman);
            }

            // 배송 경로 생성
            DeliveryRoute deliveryRoute = hubRouteModel.toDeliveryRouteEntity(delivery, deliveryman);
            deliveryRoutes.add(deliveryRoute);
            log.info("OrderCreateService assignHubDeliveryman : deliveryRoute : {}", deliveryRoute);

            // 베송 담당자 상태 ASSIGNED 로 업데이트
            deliveryman.updateAssigned(deliveryRoute.getEndHubId(), deliveryRoute.getSequence());
            assignedDeliverymans.add(deliveryman);
        }
    }

    /**
     * 슬랙 알림 발송 메서드
     *
     * @param email
     * @param requestDto
     * @param hubRouteInfoRes
     * @param orderId
     */
    private void alertExpectedTimeSlackMessage(String email, OrderAndDeliveryCreateReq requestDto, HubRouteInfoRes hubRouteInfoRes, UUID orderId) {
        log.info("OrderCreateService alertExpectedTimeSlackMessage start");

        String message = geminiUtil.getExpectedStartDateMessage().message();
        String tempMessage = """
                허브가 발송할 주문이 추가되었습니다.
                주문 ID = 
                """ + orderId;
        UserSlackResWrapper<SendSlackMessageRes> SendSlackMessageRes = slackClient.sendSlackMessage(SendSlackMessageReq.toDto(tempMessage, email));
    }

//    /**
//     * 테스트용 더미 메서드
//     * @return
//     */
//    private StartEndHubIdModel tempFeignClientGetHubIdOfCompanies(UUID startCompanyId, UUID endCompanyId) {
//        UUID dummyUuid = UUID.fromString("1e6a1a38-5ede-4873-9471-1f4bb3e1d062");
//        return new StartEndHubIdModel(dummyUuid, dummyUuid);
//    }
//
//    /**
//     * 테스트용 더미 메서드
//     * @return
//     */
//    private List<HubRouteModel> tempFeignClientGetRoutes(GetStartEndHubIdOfCompanyResWrapper hubIdModel) {
//        log.info("OrderService tempFeignClientGetRoutes");
//
//        UUID dummyUuid1 = UUID.fromString("1e6a1a38-5ede-4873-9471-1f4bb3e1d062");
//        UUID dummyUuid2 = UUID.fromString("f648799c-54f8-44e3-92dd-a620b3bf6649");
//        UUID dummyUuid3 = UUID.fromString("82c41d3d-1fc4-4b76-9b4b-bb7914201427");
//        return List.of(
//            new HubRouteModel(dummyUuid1, 0, dummyUuid1, dummyUuid1, 20, 20),
//            new HubRouteModel(dummyUuid2, 1, dummyUuid2, dummyUuid2, 20, 20),
//            new HubRouteModel(dummyUuid3, 2, dummyUuid3, dummyUuid3, 20, 20)
//        );
//    }

}
