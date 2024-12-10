package com.rush.logistic.client.company_product.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final ErrorCode errorCode;
    private final String message;
}