package com.rush.logistic.client.order_delivery.domain.delivery_route.exception;

import com.rush.logistic.client.order_delivery.global.exception.BaseException;
import com.rush.logistic.client.order_delivery.global.response.StatusCode;

public class DeliveryRouteException extends BaseException {
    public DeliveryRouteException(StatusCode code) {
        super(code);
    }

    public DeliveryRouteException(StatusCode code, String message) {
        super(code, message);
    }
}
