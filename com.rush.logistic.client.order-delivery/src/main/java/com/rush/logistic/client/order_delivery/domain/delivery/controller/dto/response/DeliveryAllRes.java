package com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.response;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record DeliveryAllRes(
//        UUID orderId,
        String status,
        UUID startHubId,
        UUID endHubId,
        String address,
        String receiverSlackId,
        UUID receiverId,
        UUID createdBy,
        ZonedDateTime createdAt,
        UUID updatedBy,
        ZonedDateTime updatedAt,
        UUID deletedBy,
        ZonedDateTime deletedAt,
        Boolean isDelete
) {
    public static DeliveryAllRes fromEntity(Delivery delivery) {
        return DeliveryAllRes.builder()
                .status(delivery.getStatus().name())
                .startHubId(delivery.getStartHubId())
                .endHubId(delivery.getEndHubId())
                .address(delivery.getAddress())
                .receiverId(delivery.getReceiverId())
                .receiverSlackId(delivery.getReceiverSlackId())
                .createdAt(delivery.getCreatedAt())
                .createdBy(delivery.getCreatedBy())
                .updatedAt(delivery.getUpdatedAt())
                .updatedBy(delivery.getUpdatedBy())
                .deletedAt(delivery.getDeletedAt())
                .deletedBy(delivery.getDeletedBy())
                .isDelete(delivery.isDelete())
                .build();
    }
}
