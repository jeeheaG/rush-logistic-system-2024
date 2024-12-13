package com.rush.logistic.client.order_delivery.domain.order.service;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryCode;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryException;
import com.rush.logistic.client.order_delivery.domain.delivery.repository.DeliveryRepository;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.delivery_route.repository.DeliveryRouteRepository;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliverymanStatusEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.exception.DeliverymanCode;
import com.rush.logistic.client.order_delivery.domain.deliveryman.exception.DeliverymanException;
import com.rush.logistic.client.order_delivery.domain.deliveryman.repository.DeliverymanRepository;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAllReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAndDeliveryCreateReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderAllRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderUpdateRes;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderCode;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderException;
import com.rush.logistic.client.order_delivery.domain.order.repository.OrderRepository;
import com.rush.logistic.client.order_delivery.domain.order.service.model.HubRouteModel;
import com.rush.logistic.client.order_delivery.domain.order.service.model.StartEndHubIdModel;
import jakarta.persistence.EntityManager;
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
public class OrderService {
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final EntityManager entityManager; // TODO : .saveAndFlush() 로 대체?
    private final DeliverymanRepository deliverymanRepository;
    private final DeliveryRouteRepository deliveryRouteRepository;

    @Transactional
    public OrderAllRes createDeliveryAndOrder(OrderAndDeliveryCreateReq requestDto) {
        // 수령, 생산 업체가 소속된 허브ID 받아옴
        StartEndHubIdModel startEndHubIdModel = tempFeignClientGetHubIdOfCompanies(requestDto.receiveCompanyId(), requestDto.produceCompanyId());
        // TODO : -> 조회 실패할 경우 예외응답


        // 주문, 배송 생성
        Delivery delivery = requestDto.deliveryInfo().toEntity(startEndHubIdModel);
        Delivery deliverySaved = deliveryRepository.save(delivery);

        Order order = requestDto.toEntity(deliverySaved);
        Order orderSaved = orderRepository.save(order);

        // 배송 경로 생성
        // 허브 경로 받아옴
        List<HubRouteModel> hubRouteModels = tempFeignClientGetRoutes(startEndHubIdModel);
        log.info("OrderService createDeliveryAndOrder : hubRouteModels[0] : {}", hubRouteModels.get(0));

        // 경로별 담당자 배정 및 배송 경로 생성
        List<Deliveryman> assignedDeliverymans = new ArrayList<>();
        List<DeliveryRoute> deliveryRoutes = new ArrayList<>();
        for (HubRouteModel hubRouteModel : hubRouteModels) {
            UUID startHubId = hubRouteModel.startHubId();

            // 배송 담당자 배정 // TODO : QueryDsl?
            Deliveryman deliveryman = null;
            Optional<Deliveryman> deliverymanOp = deliverymanRepository
                    .findTop1ByLastHubIdAndStatusOrderByLastDeliveryTimeAsc(startHubId, DeliverymanStatusEnum.IDLE);
            if (deliverymanOp.isPresent()) {
                deliveryman = deliverymanOp.get();
                log.info("OrderService createDeliveryAndOrder : deliverymanOp.isPresent() : deliveryman : {}", deliveryman);
            }
            else {
                deliveryman = deliverymanRepository.findTop1ByHubInChargeIdAndStatusOrderByLastDeliveryTimeAsc(startHubId, DeliverymanStatusEnum.IDLE)
                        .orElseThrow(() -> new DeliverymanException(DeliverymanCode.AVAILABLE_DELIVERYMAN_NOT_EXIST));
                log.info("OrderService createDeliveryAndOrder : !deliverymanOp.isPresent() : deliveryman : {}", deliveryman);
            }

            // 배송 경로 생성
            DeliveryRoute deliveryRoute = hubRouteModel.toDeliveryRouteEntity(delivery, deliveryman);
            deliveryRoutes.add(deliveryRoute);
            log.info("OrderService createDeliveryAndOrder : deliveryRoute : {}", deliveryRoute);

            // 베송 담당자 상태 ASSIGNED 로 업데이트
            deliveryman.updateAssigned(deliveryRoute.getEndHubId(), deliveryRoute.getSequence());
            assignedDeliverymans.add(deliveryman);
        }

        // 배송 담당자 영속화
        deliverymanRepository.saveAllAndFlush(assignedDeliverymans);

        // 배송 경로 영속화
        List<DeliveryRoute> savedDeliveryRoutes = deliveryRouteRepository.saveAllAndFlush(deliveryRoutes);
        log.info("OrderService createDeliveryAndOrder : savedDeliveryRoutes[0] : {}", savedDeliveryRoutes.get(0));

        return OrderAllRes.fromEntity(orderSaved, savedDeliveryRoutes);
    }

    public OrderAllRes getOrderDetail(UUID orderId) {
        Order order = getOrderEntityById(orderId);
        return OrderAllRes.fromEntity(order);
    }

    @Transactional
    public OrderUpdateRes updateOrder(UUID orderId, OrderAllReq requestDto) {
        Delivery delivery = deliveryRepository.findById(requestDto.deliveryId())
                .orElseThrow(() -> new DeliveryException(DeliveryCode.DELIVERY_NOT_EXIST));

        Order order = getOrderEntityById(orderId);
        order.updateAll(requestDto, delivery);
        entityManager.flush();

        Order orderSaved = getOrderEntityById(orderId);
        return OrderUpdateRes.fromEntity(orderSaved);
    }

    @Transactional
    public UUID deleteOrder(UUID orderId, UUID userId) {
        Order order = getOrderEntityById(orderId);
        order.softDelete(userId.toString());
        return order.getId();
    }

    private Order getOrderEntityById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderCode.ORDER_NOT_EXIST));
    }





    /**
     * 테스트용 더미 메서드
     * @return
     */
    private StartEndHubIdModel tempFeignClientGetHubIdOfCompanies(UUID startCompanyId, UUID endCompanyId) {
        UUID dummyUuid = UUID.fromString("1e6a1a38-5ede-4873-9471-1f4bb3e1d062");
        return new StartEndHubIdModel(dummyUuid, dummyUuid);
    }

    /**
     * 테스트용 더미 메서드
     * @return
     */
    private List<HubRouteModel> tempFeignClientGetRoutes(StartEndHubIdModel hubIdModel) {
        log.info("OrderService tempFeignClientGetRoutes");

        UUID dummyUuid1 = UUID.fromString("1e6a1a38-5ede-4873-9471-1f4bb3e1d062");
        UUID dummyUuid2 = UUID.fromString("f648799c-54f8-44e3-92dd-a620b3bf6649");
        UUID dummyUuid3 = UUID.fromString("82c41d3d-1fc4-4b76-9b4b-bb7914201427");
        return List.of(
            new HubRouteModel(dummyUuid1, 0, dummyUuid1, dummyUuid1, 20, 20),
            new HubRouteModel(dummyUuid2, 1, dummyUuid2, dummyUuid2, 20, 20),
            new HubRouteModel(dummyUuid3, 2, dummyUuid3, dummyUuid3, 20, 20)
        );
    }

}
