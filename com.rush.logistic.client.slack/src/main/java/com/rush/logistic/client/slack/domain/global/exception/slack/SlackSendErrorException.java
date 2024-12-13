package com.rush.logistic.client.slack.domain.global.exception.slack;

import com.rush.logistic.client.slack.domain.global.exception.common.BusinessException;
import com.rush.logistic.client.slack.domain.global.exception.common.ErrorCode;

public class SlackSendErrorException extends BusinessException {
    public SlackSendErrorException() {
        super(ErrorCode.SLACK_SEND_ERROR_EXCEPTION);
    }
}
