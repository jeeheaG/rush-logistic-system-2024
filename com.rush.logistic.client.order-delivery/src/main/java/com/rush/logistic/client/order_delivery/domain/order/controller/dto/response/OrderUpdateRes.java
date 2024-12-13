package com.rush.logistic.client.order_delivery.domain.order.controller.dto.response;

import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.response.DeliveryAllRes;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.response.DeliveryRouteCreateRes;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderUpdateRes(
        UUID orderId,
        UUID productId,
        Integer quantity,
        UUID receiveCompanyId,
        UUID produceCompanyId,
        String requestDeadLine,
        String requestNote,
        DeliveryAllRes delivery,

        String createdBy,
        ZonedDateTime createdAt,
        String updatedBy,
        ZonedDateTime updatedAt,
        String deletedBy,
        ZonedDateTime deletedAt,
        Boolean isDelete
) {
    public static OrderUpdateRes fromEntity(Order order) {
        DeliveryAllRes deliveryAllRes = DeliveryAllRes.fromEntity(order.getDelivery());

        return OrderUpdateRes.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .receiveCompanyId(order.getReceiveCompanyId())
                .produceCompanyId(order.getProduceCompanyId())
                .requestDeadLine(order.getRequestDeadLine())
                .requestNote(order.getRequestNote())
                .delivery(deliveryAllRes)

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
