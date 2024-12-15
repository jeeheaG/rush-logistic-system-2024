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
    CANNOT_RESOLVE_USER_ID(HttpStatus.UNAUTHORIZED, "사용자 식별자를 알 수 없음"),
    TOKEN_USER_ROLE_NOT_VALID(HttpStatus.UNAUTHORIZED, "사용자의 해당 권한이 더 이상 유효하지 않음"),
    USER_NOT_ALLOWED(HttpStatus.FORBIDDEN, "허가되지 않은 사용자의 접근"),
    CANNOT_CHECK_USER_ROLE_IN_CHARGE(HttpStatus.FORBIDDEN, "사용자가 해당 리소스 담당인지 알 수 없음"),

    USER_NOT_ALLOWED_HUB(HttpStatus.FORBIDDEN, "해당 허브에 접근 권한 없음"),
    USER_NOT_ALLOWED_COMPANY(HttpStatus.FORBIDDEN, "해당 업체에 접근 권한 없음"),
    USER_NOT_ALLOWED_DELIVERY(HttpStatus.FORBIDDEN, "해당 배달에 접근 권한 없음"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
