package com.rush.logistic.client.domain.global.exception.jwt;

import com.rush.logistic.client.domain.global.exception.common.BusinessException;
import com.rush.logistic.client.domain.global.exception.common.ErrorCode;

public class JwtInvalidException extends BusinessException {

    public JwtInvalidException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
