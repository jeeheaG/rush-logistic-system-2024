package com.rush.logistic.client.order_delivery.domain.order.exception;

import com.rush.logistic.client.order_delivery.global.exception.BaseException;
import com.rush.logistic.client.order_delivery.global.response.StatusCode;

public class OrderException extends BaseException {
    public OrderException(StatusCode code) {
        super(code);
    }

    public OrderException(StatusCode code, String message) {
        super(code, message);
    }
}
