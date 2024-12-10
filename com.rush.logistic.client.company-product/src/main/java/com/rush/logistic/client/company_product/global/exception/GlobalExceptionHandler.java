package com.rush.logistic.client.company_product.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Response<ErrorResponse>> handleApplicationException(ApplicationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(Response.error(errorResponse), ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<ErrorResponse>> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_REQUEST, ex.getMessage());
        return new ResponseEntity<>(Response.error(errorResponse), ErrorCode.INVALID_REQUEST.getStatus());
    }
}