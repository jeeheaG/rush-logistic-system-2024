package com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DeliveryRouteIdRes(
        UUID deliveryRouteId
) {
    public static DeliveryRouteIdRes toDto(UUID deliveryRouteId) {
        return DeliveryRouteIdRes.builder()
                .deliveryRouteId(deliveryRouteId)
                .build();
    }
}
