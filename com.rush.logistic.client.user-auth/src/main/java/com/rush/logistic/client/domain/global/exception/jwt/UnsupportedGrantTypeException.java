package com.rush.logistic.client.domain.global.exception.jwt;

import com.rush.logistic.client.domain.global.exception.common.BusinessException;
import com.rush.logistic.client.domain.global.exception.common.ErrorCode;

public class UnsupportedGrantTypeException extends BusinessException {

    public UnsupportedGrantTypeException() {
        super(ErrorCode.NOT_SUPPORTED_GRANT_TYPE_EXCEPTION);
    }
}
