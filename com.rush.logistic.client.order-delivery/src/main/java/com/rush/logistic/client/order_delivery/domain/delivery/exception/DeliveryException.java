package com.rush.logistic.client.order_delivery.domain.delivery.exception;

import com.rush.logistic.client.order_delivery.global.exception.BaseException;
import com.rush.logistic.client.order_delivery.global.response.StatusCode;

public class DeliveryException extends BaseException {
    public DeliveryException(StatusCode code) {
        super(code);
    }

    public DeliveryException(StatusCode code, String message) {
        super(code, message);
    }
}
