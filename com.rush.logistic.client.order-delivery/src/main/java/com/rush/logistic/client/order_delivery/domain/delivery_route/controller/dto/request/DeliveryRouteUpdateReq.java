package com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.request;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryRouteStatusEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryTypeEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;

import java.util.UUID;

public record DeliveryRouteUpdateReq(
//        Deliveryman deliveryman,
        Integer sequence,
        DeliveryRouteStatusEnum status,
//        Delivery delivery,
        UUID startHubId,
        UUID endHubId,
        Integer expectedDistance,
        Integer expectedTime,
        Integer realDistance,
        Integer realTime,
        DeliveryTypeEnum type
) {
//    public DeliveryRoute toEntity() {
//        return DeliveryRoute.builder()
//
//                .build();
//    }
}
