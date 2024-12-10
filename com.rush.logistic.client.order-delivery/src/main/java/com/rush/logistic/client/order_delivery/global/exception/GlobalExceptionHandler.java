package com.rush.logistic.client.order_delivery.global.exception;

import com.rush.logistic.client.order_delivery.global.response.BaseResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

import static com.rush.logistic.client.order_delivery.global.response.BasicCode.ILLEGAL_ARGUMENT_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //@Valid 로 발생하는 에러
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidatedException(ConstraintViolationException e){
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder message = new StringBuilder();
        if(constraintViolations != null) {
            for(ConstraintViolation c : constraintViolations){
                String[] paths = c.getPropertyPath().toString().split("\\.");
                String path = paths.length > 0 ? paths[paths.length - 1] : "";
                message.append(path);
                message.append(" : ");
                message.append(c.getMessage());
                message.append(". ");
            }
        }
        return ResponseEntity.status(ILLEGAL_ARGUMENT_ERROR.getHttpStatus()).body(
                BaseResponse.toResponse(
                        ILLEGAL_ARGUMENT_ERROR,
                        message.toString()
                ));
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    protected ResponseEntity<ExceptionResponse<List<InvalidParameterResponse>>> methodArgNotValidException(MethodArgumentNotValidException e) {
//        List<FieldError> errors = e.getBindingResult().getFieldErrors();
//
//        List<InvalidParameterResponse> invalidParameterResponses = errors.stream()
//                .map(InvalidParameterResponse::of)
//                .toList();
//
//        return ResponseEntity.status(ILLEGAL_ARGUMENT_ERROR.getHttpStatus()).body(
//                ExceptionResponse.of(
//                        ILLEGAL_ARGUMENT_ERROR.getHttpStatus(),
//                        ILLEGAL_ARGUMENT_ERROR.getMessage(),
//                        invalidParameterResponses));
//    }

    /**
     * [Exception] BaseException 핸들러
     *
     * @param e BaseException
     * @return ResponseEntity<BaseResponse>
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse> customExceptionHandler(BaseException e) {
        log.error("BaseException: " + e);
        return ResponseEntity.status(e.getCode().getHttpStatus()).body(
                BaseResponse.toResponse(
                        e.getCode(),
                        e.getCode().getMessage()
                )
        );
    }

    /**
     * [Exception] RuntimeException 핸들러
     *
     * @param e RuntimeException
     * @return ResponseEntity<BaseResponse>
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException: ", e);
        return ResponseEntity.internalServerError().body(
                BaseResponse.toResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
        );
    }
}
