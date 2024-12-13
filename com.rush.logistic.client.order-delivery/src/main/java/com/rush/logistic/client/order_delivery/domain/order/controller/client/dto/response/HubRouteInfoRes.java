package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record HubRouteInfoRes(
        UUID startHubId,
        UUID endHubId,
        String startHubName,
        String endHubName,
        String startHubAddress,
        String endHubAddress,
        String timeTaken,
        Integer distance,
        String milliseconds
) {
}
