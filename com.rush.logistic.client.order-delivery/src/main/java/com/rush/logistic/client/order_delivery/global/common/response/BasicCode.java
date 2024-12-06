package com.rush.logistic.client.order_delivery.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BasicCode implements StatusCode{
    //2xx
    TEST_OK(HttpStatus.OK, "성공 (test)"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
