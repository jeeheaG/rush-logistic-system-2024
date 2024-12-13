package com.rush.logistic.client.domain.global.exception.user;

import com.rush.logistic.client.domain.global.exception.common.BusinessException;
import com.rush.logistic.client.domain.global.exception.common.ErrorCode;

public class FailedLoginException extends BusinessException {
    public FailedLoginException() {
        super(ErrorCode.FAILED_LOGIN_EXCEPTION);
    }
}
