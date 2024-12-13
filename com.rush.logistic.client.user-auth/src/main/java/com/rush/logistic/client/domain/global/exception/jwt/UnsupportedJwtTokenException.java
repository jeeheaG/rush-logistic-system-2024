package com.rush.logistic.client.domain.global.exception.jwt;

import com.rush.logistic.client.domain.global.exception.common.BusinessException;
import com.rush.logistic.client.domain.global.exception.common.ErrorCode;

public class UnsupportedJwtTokenException extends BusinessException {
    public UnsupportedJwtTokenException() {
        super(ErrorCode.UNSUPPORTED_JWT_TOKEN_EXCEPTION);
    }
}
