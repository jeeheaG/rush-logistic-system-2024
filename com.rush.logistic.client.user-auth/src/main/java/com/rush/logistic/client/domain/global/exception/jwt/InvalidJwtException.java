package com.rush.logistic.client.domain.global.exception.jwt;

import com.rush.logistic.client.domain.global.exception.common.BusinessException;
import com.rush.logistic.client.domain.global.exception.common.ErrorCode;

public class InvalidJwtException extends BusinessException {
    public InvalidJwtException() {
        super(ErrorCode.INVALID_JWT_EXCEPTION);
    }
}
