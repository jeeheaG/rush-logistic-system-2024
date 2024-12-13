package com.rush.logistic.client.domain.global.exception.common;

public class UnAuthorizedModifyException extends BusinessException{

    public UnAuthorizedModifyException() {
        super(ErrorCode.UNAUTHORIZED_MODIFY_EXCEPTION);
    }
}
