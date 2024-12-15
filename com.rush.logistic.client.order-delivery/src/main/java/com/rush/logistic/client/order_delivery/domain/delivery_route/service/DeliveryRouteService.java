package com.rush.logistic.client.order_delivery.domain.delivery_route.service;

import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.request.DeliveryRouteUpdateReq;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.response.DeliveryRouteAllRes;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.delivery_route.exception.DeliveryRouteCode;
import com.rush.logistic.client.order_delivery.domain.delivery_route.exception.DeliveryRouteException;
import com.rush.logistic.client.order_delivery.domain.delivery_route.repository.DeliveryRouteRepository;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.global.auth.checker.DeliveryRouteUserRoleChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DeliveryRouteService {
    private final DeliveryRouteRepository deliveryRouteRepository;
    private final DeliveryRouteUserRoleChecker deliveryRouteUserRoleChecker;

//    /**
//     * api없이 주문or배달 쪽에서 내부호출 될 예정
//     * @param requestDto
//     * @return
//     */
//    @Transactional
//    public DeliveryRouteAllRes createDeliveryRoute(DeliveryRouteCreateReq requestDto) {
//        DeliveryRoute deliveryRouteSaved = deliveryRouteRepository.save(requestDto.toEntity());
//        return DeliveryRouteAllRes.fromEntity(deliveryRouteSaved);
//    }

    public DeliveryRouteAllRes getDeliveryRouteDetail(UUID deliveryRouteId, GetUserInfoRes getUserInfoRes) {
        DeliveryRoute deliveryRoute = getDeliveryRouteEntityById(deliveryRouteId);
        deliveryRouteUserRoleChecker.checkInCharge(deliveryRoute, getUserInfoRes);

        return DeliveryRouteAllRes.fromEntity(deliveryRoute);
    }

    @Transactional
    public DeliveryRouteAllRes updateDeliveryRoute(UUID deliveryRouteId, DeliveryRouteUpdateReq requestDto, GetUserInfoRes getUserInfoRes) {
        DeliveryRoute deliveryRoute = getDeliveryRouteEntityById(deliveryRouteId);
        deliveryRouteUserRoleChecker.checkInCharge(deliveryRoute, getUserInfoRes);

        deliveryRoute.updateAll(requestDto);
        DeliveryRoute savedDeliveryRoute = deliveryRouteRepository.saveAndFlush(deliveryRoute);
        return DeliveryRouteAllRes.fromEntity(savedDeliveryRoute);
    }

    @Transactional
    public UUID deleteDeliveryRoute(UUID deliveryRouteId, Long deleteUserId, GetUserInfoRes getUserInfoRes) {
        DeliveryRoute deliveryRoute = getDeliveryRouteEntityById(deliveryRouteId);
        deliveryRouteUserRoleChecker.checkInCharge(deliveryRoute, getUserInfoRes);

        deliveryRoute.softDelete(deleteUserId.toString());
        return deliveryRoute.getId();
    }


    private DeliveryRoute getDeliveryRouteEntityById(UUID deliveryRouteId) {
        return deliveryRouteRepository.findById(deliveryRouteId)
                .orElseThrow(() -> new DeliveryRouteException(DeliveryRouteCode.DELIVERY_ROUTE_NOT_EXIST));
    }
}
