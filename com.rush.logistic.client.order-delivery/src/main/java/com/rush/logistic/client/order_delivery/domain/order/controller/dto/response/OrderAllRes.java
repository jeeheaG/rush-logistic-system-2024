package com.rush.logistic.client.order_delivery.domain.order.controller.dto.response;

import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record OrderAllRes(
        UUID orderId,
        UUID productId,
        Integer quantity,
        UUID receiveCompanyId,
        UUID produceCompanyId,
        UUID deliveryId,
        String requestDeadLine,
        String requestNote,
        UUID createdBy,
        ZonedDateTime createdAt,
        UUID updatedBy,
        ZonedDateTime updatedAt,
        UUID deletedBy,
        ZonedDateTime deletedAt,
        Boolean isDelete
) {
    public static OrderAllRes fromEntity(Order order) {
        return OrderAllRes.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .receiveCompanyId(order.getReceiveCompanyId())
                .produceCompanyId(order.getProduceCompanyId())
                .deliveryId(order.getDeliveryId())
                .requestDeadLine(order.getRequestDeadLine())
                .requestNote(order.getRequestNote())
                .createdAt(order.getCreatedAt())
                .createdBy(order.getCreatedBy())
                .updatedAt(order.getUpdatedAt())
                .updatedBy(order.getUpdatedBy())
                .deletedAt(order.getDeletedAt())
                .deletedBy(order.getDeletedBy())
                .isDelete(order.isDelete())
                .build();
    }
}
