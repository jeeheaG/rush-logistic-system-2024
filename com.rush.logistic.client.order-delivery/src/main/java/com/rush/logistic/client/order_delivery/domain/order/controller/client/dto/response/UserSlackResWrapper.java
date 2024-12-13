package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response;

import lombok.Builder;

@Builder
public record UserSlackResWrapper<T> (
        Boolean success,
        String httpStatus,
        String errorMessage,
        T data
) {
}
