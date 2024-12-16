package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record HubResSubWrapper<T> (
        Integer totalDistance,
        Long totalMilliseconds,
        String totalTimeTaken,
        List<T> hubRouteList
) {
}
