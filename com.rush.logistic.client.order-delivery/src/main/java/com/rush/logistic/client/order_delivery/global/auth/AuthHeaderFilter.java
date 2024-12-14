//package com.rush.logistic.client.order_delivery.global.auth;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//import static com.rush.logistic.client.order_delivery.global.common.GlobalConst.*;
//
//@Component
//public class AuthHeaderFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        // 헤더에서 추출
//        String userId = request.getHeader(HEADER_USER_ID);
//        String username = request.getHeader(HEADER_USER_NAME);
//        String role = request.getHeader(HEADER_ROLE);
//
//        // 세션에 저장
//        request.setAttribute(HEADER_USER_ID, userId);
//        request.setAttribute(HEADER_USER_NAME, username);
//        request.setAttribute(HEADER_ROLE, role);
//
//        filterChain.doFilter(request, response);
//    }
//}
