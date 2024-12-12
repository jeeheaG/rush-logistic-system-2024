package com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DeliverymanIdRes(
        Long userId
) {
    public static DeliverymanIdRes toDto(Long userId) {
        return DeliverymanIdRes.builder()
                .userId(userId)
                .build();
    }
}
