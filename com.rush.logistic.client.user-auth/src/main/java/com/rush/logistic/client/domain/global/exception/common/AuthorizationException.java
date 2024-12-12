package com.rush.logistic.client.domain.global.exception.common;

public class AuthorizationException extends BusinessException{

    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
