package com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.request;

import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliverymanStatusEnum;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;


@Builder
public record DeliverymanUpdateReq(
        DeliverymanStatusEnum status,
        Integer sequence,
        UUID hubInChargeId,
        UUID lastHubId,
        ZonedDateTime lastDeliveryTime
) {
//    public Deliveryman toEntity() {
//        return Deliveryman.builder()
//
//                .build();
//    }
}
