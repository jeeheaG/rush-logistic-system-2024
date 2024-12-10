package com.rush.logistic.client.company_product.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    DUPLICATED_COMPANYNAME(HttpStatus.CONFLICT, "업체 이름이 중복됩니다.");
    private HttpStatus status;
    private String message;

}
