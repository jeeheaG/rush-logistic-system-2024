package com.rush.logistic.client.order_delivery.global.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import static com.rush.logistic.client.order_delivery.global.common.GlobalConst.*;

/**
 * feign client 요청 시 공통 헤더를 달아주는 interceptor
 */
@RequiredArgsConstructor
public class AuthHeaderInterceptor implements RequestInterceptor {
    HttpServletRequest servletRequest;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String userId = servletRequest.getHeader(HEADER_USER_ID);
        if (userId==null) {
            userId = "";
        }
        requestTemplate.header(HEADER_USER_ID, userId);

        String role = servletRequest.getHeader(HEADER_ROLE);
        if (role==null) {
            role = "";
        }
        requestTemplate.header(HEADER_ROLE, role);

        String username = servletRequest.getHeader(HEADER_USER_NAME);
        if (username==null) {
            username = "";
        }
        requestTemplate.header(HEADER_USER_NAME, username);
    }
}
