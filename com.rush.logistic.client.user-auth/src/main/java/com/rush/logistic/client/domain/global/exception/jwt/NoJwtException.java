package com.rush.logistic.client.domain.global.exception.jwt;

import com.rush.logistic.client.domain.global.exception.common.BusinessException;
import com.rush.logistic.client.domain.global.exception.common.ErrorCode;

public class NoJwtException extends BusinessException {

    public NoJwtException() {
        super(ErrorCode.NO_JWT_EXCEPTION);
    }
}
