package com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response;

import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record DeliverymanAllRes(

        ZonedDateTime createdAt,
        UUID createdBy,
        ZonedDateTime updatedAt,
        UUID updatedBy,
        ZonedDateTime deletedAt,
        UUID deletedBy,
        Boolean isDelete
) {
    public static DeliverymanAllRes fromEntity(Deliveryman deliveryman) {

        return DeliverymanAllRes.builder()

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
