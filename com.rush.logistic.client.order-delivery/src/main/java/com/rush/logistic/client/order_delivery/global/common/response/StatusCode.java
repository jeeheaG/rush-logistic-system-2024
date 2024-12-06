package com.rush.logistic.client.order_delivery.global.common.response;

import org.springframework.http.HttpStatus;

public interface StatusCode {
    String getName();
    HttpStatus getHttpStatus();
    String getMessage();
}
