package com.rush.logistic.client.order_delivery.domain.deliveryman.exception;

import com.rush.logistic.client.order_delivery.global.exception.BaseException;
import com.rush.logistic.client.order_delivery.global.response.StatusCode;

public class DeliverymanException extends BaseException {
    public DeliverymanException(StatusCode code) {
        super(code);
    }

    public DeliverymanException(StatusCode code, String message) {
        super(code, message);
    }
}
