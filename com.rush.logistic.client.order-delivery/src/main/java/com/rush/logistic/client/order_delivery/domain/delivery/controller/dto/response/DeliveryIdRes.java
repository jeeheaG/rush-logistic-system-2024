package com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DeliveryIdRes(
        UUID orderId
) {
    public static DeliveryIdRes toDto(UUID deliveryId) {
        return DeliveryIdRes.builder()
                .orderId(deliveryId)
                .build();
    }
}
