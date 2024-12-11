package com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DeliverymanIdRes(
        UUID DeliverymanId
) {
    public static DeliverymanIdRes toDto(UUID DeliverymanId) {
        return DeliverymanIdRes.builder()
                .DeliverymanId(DeliverymanId)
                .build();
    }
}
