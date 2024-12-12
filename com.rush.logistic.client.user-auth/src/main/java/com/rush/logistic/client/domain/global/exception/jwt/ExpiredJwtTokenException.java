package com.rush.logistic.client.domain.global.exception.jwt;

import com.rush.logistic.client.domain.global.exception.common.BusinessException;
import com.rush.logistic.client.domain.global.exception.common.ErrorCode;

public class ExpiredJwtTokenException extends BusinessException {
    public ExpiredJwtTokenException() {
        super(ErrorCode.EXPIRED_JWT_TOKEN_EXCEPTION);
    }
}
