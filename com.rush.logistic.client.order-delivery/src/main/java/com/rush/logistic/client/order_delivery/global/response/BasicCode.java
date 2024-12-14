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
    CANNOT_RESOLVE_USER_ROLE(HttpStatus.UNAUTHORIZED, "사용자 권한을 알 수 없음"),
    CANNOT_RESOLVE_USER_ID(HttpStatus.UNAUTHORIZED, "사용자 식별자를 알 수 없음");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
