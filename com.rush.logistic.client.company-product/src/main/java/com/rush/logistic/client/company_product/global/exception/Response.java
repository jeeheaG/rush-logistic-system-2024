package com.rush.logistic.client.company_product.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Response<T> {
    private String resultCode;
    private T result;
    private String msg;

    public static <T> Response<T> success(T result, String msg) {
        return Response.<T>builder()
                .resultCode("SUCCESS")
                .result(result)
                .msg(msg)
                .build();
    }

    public static <T> Response<T> error(T result) {
        return Response.<T>builder()
                .resultCode("ERROR")
                .result(result)
                .build();
    }
}