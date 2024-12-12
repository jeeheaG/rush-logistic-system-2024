package com.rush.logistic.client.domain.global.exception.jwt;

import com.rush.logistic.client.domain.global.exception.common.BusinessException;
import com.rush.logistic.client.domain.global.exception.common.ErrorCode;

public class InvalidJwtSignatureException extends BusinessException {
    public InvalidJwtSignatureException() {
        super(ErrorCode.INVALID_JWT_SIGNATURE_EXCEPTION);
    }
}
