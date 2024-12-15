package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserSlackResWrapper<T> (
        Boolean success,
        String httpStatus,
        List<String> errorMessage,
        T data
) {
}
