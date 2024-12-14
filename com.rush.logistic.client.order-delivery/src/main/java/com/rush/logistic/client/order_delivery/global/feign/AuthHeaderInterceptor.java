package com.rush.logistic.client.order_delivery.global.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.rush.logistic.client.order_delivery.global.common.GlobalConst.*;

/**
 * feign client 요청 시 공통 헤더를 달아주는 interceptor
 */
@Slf4j
@RequiredArgsConstructor
public class AuthHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes!=null) {
            HttpServletRequest servletRequest = attributes.getRequest();


            String userId = servletRequest.getHeader(HEADER_USER_ID);
            if (userId==null) {
                userId = "";
            }
            requestTemplate.header(HEADER_USER_ID, userId);
            log.info("AuthHeaderInterceptor : set header : {}={}", HEADER_USER_ID, userId);

            String role = servletRequest.getHeader(HEADER_ROLE);
            if (role==null) {
                role = "";
            }
            requestTemplate.header(HEADER_ROLE, role);
            log.info("AuthHeaderInterceptor : set header : {}={}", HEADER_ROLE, role);

            String username = servletRequest.getHeader(HEADER_USER_NAME);
            if (username==null) {
                username = "";
            }
            requestTemplate.header(HEADER_USER_NAME, username);
            log.info("AuthHeaderInterceptor : set header : {}={}", HEADER_USER_NAME, username);

        }

    }
}
