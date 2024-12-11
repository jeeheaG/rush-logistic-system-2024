package com.rush.logistic.client.order_delivery.domain.order.controller.dto.request;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderAllReq(
        UUID productId,
        Integer quantity,
        UUID receiveCompanyId,
        UUID produceCompanyId,
        UUID deliveryId,
        String requestDeadLine,
        String requestNote
) {
    public Order toEntity(Delivery delivery) {
        return Order.builder()
                .productId(this.productId)
                .quantity(this.quantity)
                .receiveCompanyId(this.receiveCompanyId)
                .produceCompanyId(this.produceCompanyId)
                .delivery(delivery)
                .requestDeadLine(this.requestDeadLine)
                .requestNote(this.requestNote)
                .build();
    }
}
