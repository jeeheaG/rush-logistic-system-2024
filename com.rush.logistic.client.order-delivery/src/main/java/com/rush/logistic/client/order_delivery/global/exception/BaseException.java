package com.rush.logistic.client.order_delivery.global.exception;

import com.rush.logistic.client.order_delivery.global.response.StatusCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final StatusCode code;
    private final String message;

    public BaseException(StatusCode code) {
        this.code = code;
        this.message = code.getMessage();
    }

    public BaseException(StatusCode code, String message) {
        this.code = code;
        this.message = message;
    }
}
