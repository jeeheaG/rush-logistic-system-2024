package com.rush.logistic.client.slack.domain.global.exception.slack;

import com.rush.logistic.client.slack.domain.global.exception.common.BusinessException;
import com.rush.logistic.client.slack.domain.global.exception.common.ErrorCode;

public class NotFoundSlackIdException extends BusinessException {
    public NotFoundSlackIdException() {
        super(ErrorCode.NOT_FOUND_SLACK_ID_EXCEPTION);
    }
}
