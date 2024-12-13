package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response;

import lombok.Builder;

@Builder
public record HubResWrapper<T> (
        Integer status,
        String code,
        String message,
        T data
) {
}
