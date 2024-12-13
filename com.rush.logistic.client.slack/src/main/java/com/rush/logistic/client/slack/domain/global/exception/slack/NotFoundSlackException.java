package com.rush.logistic.client.slack.domain.global.exception.slack;

import com.rush.logistic.client.slack.domain.global.exception.common.BusinessException;
import com.rush.logistic.client.slack.domain.global.exception.common.ErrorCode;

public class NotFoundSlackException extends BusinessException {
    public NotFoundSlackException() {
        super(ErrorCode.NO_AUTHORIZATION_EXCEPTION);
    }
}
