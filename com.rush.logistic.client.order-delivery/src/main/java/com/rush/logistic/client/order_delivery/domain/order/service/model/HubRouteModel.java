package com.rush.logistic.client.order_delivery.domain.order.service.model;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryRouteStatusEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryTypeEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;

import java.util.UUID;

public record HubRouteModel(
    UUID id,
    Integer sequence,
    UUID startHubId,
    UUID endHubId,
    Integer expectedDistance,
    Integer expectedTime
) {

    public DeliveryRoute toDeliveryRouteEntity(Delivery delivery, Deliveryman deliveryman) {
        return DeliveryRoute.builder()
                .sequence(this.sequence)
                .status(DeliveryRouteStatusEnum.WAITING)
                .startHubId(this.startHubId)
                .endHubId(this.endHubId)
                .expectedDistance(this.expectedDistance)
                .expectedTime(this.expectedTime)
                .type(DeliveryTypeEnum.HUB)
                .delivery(delivery)
                .deliveryman(deliveryman)
                .build();
    }
}
