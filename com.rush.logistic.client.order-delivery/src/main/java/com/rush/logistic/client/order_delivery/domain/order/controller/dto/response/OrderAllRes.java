package com.rush.logistic.client.order_delivery.domain.order.controller.dto.response;

import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.response.DeliveryAllRes;
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
        DeliveryAllRes delivery,
        String requestDeadLine,
        String requestNote,
        String createdBy,
        ZonedDateTime createdAt,
        String updatedBy,
        ZonedDateTime updatedAt,
        String deletedBy,
        ZonedDateTime deletedAt,
        Boolean isDelete
) {
    public static OrderAllRes fromEntity(Order order) {
        DeliveryAllRes deliveryAllRes = DeliveryAllRes.fromEntity(order.getDelivery());

        return OrderAllRes.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .receiveCompanyId(order.getReceiveCompanyId())
                .produceCompanyId(order.getProduceCompanyId())
                .delivery(deliveryAllRes)
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
