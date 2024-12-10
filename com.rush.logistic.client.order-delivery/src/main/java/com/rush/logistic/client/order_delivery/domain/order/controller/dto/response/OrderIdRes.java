package com.rush.logistic.client.order_delivery.domain.order.controller.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderIdRes(
        UUID orderId
) {
    public static OrderIdRes toDto(UUID orderId) {
        return OrderIdRes.builder()
                .orderId(orderId)
                .build();
    }
}
