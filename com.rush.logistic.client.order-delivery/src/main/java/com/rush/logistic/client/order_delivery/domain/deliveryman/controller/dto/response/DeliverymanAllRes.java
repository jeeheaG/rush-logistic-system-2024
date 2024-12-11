package com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response;

import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliverymanInChargeTypeEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliverymanStatusEnum;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record DeliverymanAllRes(
//        UUID deliverymanId,
        Long userId,
        DeliverymanInChargeTypeEnum inChargeType,
        DeliverymanStatusEnum status,
        Integer sequence,
        UUID hubInChargeId,
        UUID lastHubId,
        ZonedDateTime lastDeliveryTime,
        ZonedDateTime createdAt,
        String createdBy,
        ZonedDateTime updatedAt,
        String updatedBy,
        ZonedDateTime deletedAt,
        String deletedBy,
        Boolean isDelete
) {
    public static DeliverymanAllRes fromEntity(Deliveryman deliveryman) {

        return DeliverymanAllRes.builder()
//                .deliverymanId(deliveryman.getId())
                .userId(deliveryman.getUserId())
                .inChargeType(deliveryman.getInChargeType())
                .status(deliveryman.getStatus())
                .sequence(deliveryman.getSequence())
                .hubInChargeId(deliveryman.getHubInChargeId())
                .lastHubId(deliveryman.getLastHubId())
                .lastDeliveryTime(deliveryman.getLastDeliveryTime())
                .createdAt(deliveryman.getCreatedAt())
                .createdBy(deliveryman.getCreatedBy())
                .updatedAt(deliveryman.getUpdatedAt())
                .updatedBy(deliveryman.getUpdatedBy())
                .deletedAt(deliveryman.getDeletedAt())
                .deletedBy(deliveryman.getDeletedBy())
                .isDelete(deliveryman.isDelete())
                .build();
    }
}
