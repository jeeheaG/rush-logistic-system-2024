package com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.request;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryRouteStatusEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryTypeEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;

import java.util.UUID;

public record DeliveryRouteCreateReq(
//        Deliveryman deliveryman,
        Integer sequence,
//        Delivery delivery,
        UUID startHubId,
        UUID endHubId,
        Integer expectedDistance,
        Integer expectedTime,
        DeliveryTypeEnum type
) {
    public DeliveryRoute toEntity() {
        return DeliveryRoute.builder()
//                .deliveryman(deliveryman)
                .sequence(sequence)
                .status(DeliveryRouteStatusEnum.WAITING)
//                .delivery(delivery)
                .startHubId(startHubId)
                .endHubId(endHubId)
                .expectedDistance(expectedDistance)
                .expectedTime(expectedTime)
                .type(type)
                .build();
    }
}
