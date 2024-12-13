package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response;

public record CompanyResWrapper<T> (
        String resultCode,
        T result,
        String msg
) {
}
