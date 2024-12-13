package com.rush.logistic.client.order_delivery.domain.delivery_route.service;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.request.DeliveryRouteCreateReq;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.request.DeliveryRouteUpdateReq;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.response.DeliveryRouteAllRes;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.delivery_route.exception.DeliveryRouteCode;
import com.rush.logistic.client.order_delivery.domain.delivery_route.exception.DeliveryRouteException;
import com.rush.logistic.client.order_delivery.domain.delivery_route.repository.DeliveryRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DeliveryRouteService {
    private final DeliveryRouteRepository deliveryRouteRepository;

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

    public DeliveryRouteAllRes getDeliveryRouteDetail(UUID deliveryRouteId) {
        DeliveryRoute deliveryRoute = getDeliveryRouteEntityById(deliveryRouteId);
        return DeliveryRouteAllRes.fromEntity(deliveryRoute);
    }

    @Transactional
    public DeliveryRouteAllRes updateDeliveryRoute(UUID deliveryRouteId, DeliveryRouteUpdateReq requestDto) {
        DeliveryRoute deliveryRoute = getDeliveryRouteEntityById(deliveryRouteId);
        deliveryRoute.updateAll(requestDto);
        DeliveryRoute savedDeliveryRoute = deliveryRouteRepository.saveAndFlush(deliveryRoute);
        return DeliveryRouteAllRes.fromEntity(savedDeliveryRoute);
    }

    @Transactional
    public UUID deleteDeliveryRoute(UUID deliveryRouteId, Long deleteUserId) {
        DeliveryRoute deliveryRoute = getDeliveryRouteEntityById(deliveryRouteId);
        deliveryRoute.softDelete(deleteUserId.toString());
        return deliveryRoute.getId();
    }


    private DeliveryRoute getDeliveryRouteEntityById(UUID deliveryRouteId) {
        return deliveryRouteRepository.findById(deliveryRouteId)
                .orElseThrow(() -> new DeliveryRouteException(DeliveryRouteCode.DELIVERY_ROUTE_NOT_EXIST));
    }
}
