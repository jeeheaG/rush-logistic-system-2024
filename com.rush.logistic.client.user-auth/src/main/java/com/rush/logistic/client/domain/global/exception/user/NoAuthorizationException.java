package com.rush.logistic.client.domain.global.exception.user;

import com.rush.logistic.client.domain.global.exception.common.BusinessException;
import com.rush.logistic.client.domain.global.exception.common.ErrorCode;


public class NoAuthorizationException extends BusinessException {
    public NoAuthorizationException() {
        super(ErrorCode.NO_AUTHORIZATION_EXCEPTION);
    }
}
