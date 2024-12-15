package com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.request;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryRouteStatusEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryTypeEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import com.rush.logistic.client.order_delivery.domain.order.service.model.HubRouteModel;
import lombok.Builder;

import java.util.UUID;

@Builder
public record DeliveryRouteCreateReq(
        Deliveryman deliveryman,
        Integer sequence,
        Delivery delivery,
        UUID startHubId,
        UUID endHubId,
        Integer expectedDistance,
        String expectedTime,
        DeliveryTypeEnum type
) {
    public static DeliveryRouteCreateReq toDto(Deliveryman deliveryman, Delivery delivery, HubRouteModel hubRouteModel) {
        return DeliveryRouteCreateReq.builder()
                .deliveryman(deliveryman)
                .sequence(hubRouteModel.sequence())
                .delivery(delivery)
                .startHubId(hubRouteModel.startHubId())
                .endHubId(hubRouteModel.endHubId())
                .expectedDistance(hubRouteModel.expectedDistance())
                .expectedTime(hubRouteModel.expectedTime())
                .type(DeliveryTypeEnum.HUB)
                .build();
    }

    public DeliveryRoute toEntity() {
        return DeliveryRoute.builder()
                .deliveryman(deliveryman)
                .sequence(sequence)
                .status(DeliveryRouteStatusEnum.WAITING)
                .delivery(delivery)
                .startHubId(startHubId)
                .endHubId(endHubId)
                .expectedDistance(expectedDistance)
                .expectedTime(expectedTime)
                .type(type)
                .build();
    }
}
