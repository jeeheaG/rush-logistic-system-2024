package com.rush.logistic.client.domain.global.exception.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // JWT
    INVALID_JWT_SIGNATURE_EXCEPTION(401, "잘못된 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN_EXCEPTION(401, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN_EXCEPTION(401, "지원되지 않는 JWT 토큰입니다."),
    INVALID_JWT_EXCEPTION(401, "JWT 토큰이 잘못되었습니다"),
    INVALID_REFRESH_TOKEN_EXCEPTION(401, "RefreshToken이 유효하지 않습니다."),
    NOT_MISMATCHED_REFRESH_TOKEN_EXCEPTION(401, "DB의 리프레쉬 토큰 값과 다릅니다."),
    NO_JWT_EXCEPTION(401, "이 요청은 JWT가 필요합니다."),
    NOT_SUPPORTED_GRANT_TYPE_EXCEPTION(401, "지원하지 않는 권한 부여 유형입니다."),

    // 유저
    DUPLICATE_ENTRY_EXCEPTION(401, "중복된 사용자입니다. 다른 이름 또는 이메일을 입력해주세요."),
    SIGNUP_BLANK_EXCEPTION(401, "회원 가입란은 모두 필수입니다."),
    NOT_FOUND_USER_EXCEPTION(401, "회원 정보를 찾을 수 없습니다."),
    FAILED_AUTHENTICATION_EXCEPTION(401, "인증에 실패하였습니다."),
    ALREADY_EXIST_USERNAME_EXCEPTION(409, "이미 존재하는 이름입니다."),
    ALREADY_EXIST_EMAIL_EXCEPTION(409, "이미 존재하는 이메일입니다."),
    ALREADY_EXIST_USER_EXCEPTION(409, "이미 존재하는 유저입니다."),
    NOT_AVAILABLE_USERNAME_EXCEPTION(409, "사용할 수 없는 이름입니다."),
    NOT_AVAILABLE_EMAIL_EXCEPTION(409, "사용할 수 없는 이메일입니다."),
    UNAUTHORIZED_MODIFY_EXCEPTION(403, "수정할 권한이 없습니다."),
    NO_AUTHORIZATION_EXCEPTION(403, "접근 권한이 없습니다"),
    MISMATCHED_PASSWORD_EXCEPTION(401, "비밀번호가 일치하지 않습니다."),
    FAILED_LOGIN_EXCEPTION(401, "닉네임 또는 패스워드를 확인해주세요."),
    MISMATCHED_NEWPASSWORD_EXCEPTION(401, "새 비밀번호가 일치하지 않습니다."),
    MATCHED_PASSWORD_EXCEPTION(401, "새 비밀번호와 기존 비밀번호가 일치합니다."),
    JSON_PROCESSING_EXCEPTION(408, "요청 응답 시간 초과");



    private final int status;

    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
