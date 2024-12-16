package com.rush.logistic.client.company_product.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    DUPLICATED_COMPANYNAME(HttpStatus.CONFLICT, "업체 이름이 중복됩니다."),
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 업체를 찾지 못하였습니다."),
    COMPANY_DELETED(HttpStatus.GONE, "해당 업체는 삭제되었습니다."),

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾지 못하였습니다."),
    DUPLICATED_PRODUCTNAME(HttpStatus.CONFLICT, "상품 이름이 중복됩니다."),
    PRODUCT_DELETED(HttpStatus.GONE, "해당 상품은 삭제되었습니다."),

    INVALID_ROLE(HttpStatus.FORBIDDEN, "해당 업무의 권한이 없습니다.");

    private HttpStatus status;
    private String message;

}
