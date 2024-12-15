package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record HubRouteInfoReq(
        UUID startHubId,
        UUID endHubId
) {
    public static HubRouteInfoReq toDto(UUID startHubId, UUID endHubId) {
        return HubRouteInfoReq.builder()
                .startHubId(startHubId)
                .endHubId(endHubId)
                .build();
    }

}
