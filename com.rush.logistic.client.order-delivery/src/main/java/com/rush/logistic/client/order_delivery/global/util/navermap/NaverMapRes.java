package com.rush.logistic.client.order_delivery.global.util.navermap;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryRouteStatusEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryTypeEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import lombok.Builder;

import java.util.UUID;

@Builder
public record NaverMapRes(
        Integer distance,
        String time

) {
    public static NaverMapRes toDto() {
        return NaverMapRes.builder()
                .distance(-1)
                .time("dummyTime")
                .build();
    }

    public DeliveryRoute toDeliveryRouteEntity(Integer sequence, UUID startHubId, Delivery delivery, Deliveryman deliveryman) {
        return DeliveryRoute.builder()
                .sequence(sequence)
                .delivery(delivery)
                .deliveryman(deliveryman)
                .type(DeliveryTypeEnum.COMPANY)
                .expectedDistance(this.distance)
                .expectedTime(this.time)
                .status(DeliveryRouteStatusEnum.WAITING)
                .startHubId(startHubId)
                .build();
    }
}
