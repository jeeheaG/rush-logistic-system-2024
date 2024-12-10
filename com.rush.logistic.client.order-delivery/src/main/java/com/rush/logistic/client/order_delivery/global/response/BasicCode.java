package com.rush.logistic.client.order_delivery.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BasicCode implements StatusCode{
    //2xx
    TEST_OK(HttpStatus.OK, "성공 (test)"),

    ILLEGAL_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
