package com.rush.logistic.client.slack.domain.global.exception.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // SLACK
    NOT_FOUND_SLACK_EXCEPTION(401, "유효하지 않은 SLACK 입니다."),
    NOT_FOUND_SLACK_ID_EXCEPTION(401, "SLACK ID 를 찾을 수 없습니다."),
    SLACK_SEND_ERROR_EXCEPTION(401, "슬랙 메시지 발송에 실패하였습니다."),
    NO_AUTHORIZATION_EXCEPTION(403, "접근 권한이 없습니다.");


    private final int status;

    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
