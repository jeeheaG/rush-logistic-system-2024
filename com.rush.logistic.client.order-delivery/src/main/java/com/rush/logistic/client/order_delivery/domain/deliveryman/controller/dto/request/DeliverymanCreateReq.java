package com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.request;

import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliverymanInChargeTypeEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliverymanStatusEnum;
import lombok.Builder;

import java.util.UUID;


@Builder
public record DeliverymanCreateReq(
        Long userId,
        DeliverymanInChargeTypeEnum inChargeType,
        UUID hubInChargeId
) {
    public Deliveryman toEntity() {
        return Deliveryman.builder()
                .userId(userId)
                .inChargeType(inChargeType)
                .hubInChargeId(hubInChargeId)
                .status(DeliverymanStatusEnum.IDLE)
                .build();
    }
}
