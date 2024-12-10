package com.rush.logistic.client.order_delivery.domain.order.controller.dto.request;

import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateOrderReq(
        UUID productId,
        Integer quantity,
        UUID receiveCompanyId,
        UUID produceCompanyId,
        UUID deliveryId,
        String requestDeadLine,
        String requestNote
) {
    public Order toEntity() {
        return Order.builder()
                .productId(this.productId)
                .quantity(this.quantity)
                .receiveCompanyId(this.receiveCompanyId)
                .produceCompanyId(this.produceCompanyId)
                .deliveryId(this.deliveryId)
                .requestDeadLine(this.requestDeadLine)
                .requestNote(this.requestNote)
                .build();
    }
}
