package com.rush.logistic.client.order_delivery.global.auth;

import com.rush.logistic.client.order_delivery.global.exception.BaseException;
import com.rush.logistic.client.order_delivery.global.response.BasicCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.rush.logistic.client.order_delivery.global.common.GlobalConst.*;

@Slf4j
@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(UserInfoHeader.class) != null
                && parameter.getParameterType() == UserInfo.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String userIdStr = request.getHeader(HEADER_USER_ID);
        String username = request.getHeader(HEADER_USER_NAME);
        String role = request.getHeader(HEADER_ROLE);

        log.info("AuthArgumentResolver resolveArgument : userId={}, username={}, role={}", userIdStr, username, role);

        return new UserInfo(
                parseUserId(userIdStr),
                username,
                parseUserRole(role)
        );

    }

    private static UserRole parseUserRole(String role) {
        UserRole userRole;
        try {
            userRole = UserRole.valueOf(role);
        } catch (Exception e) {
            throw new BaseException(BasicCode.CANNOT_RESOLVE_USER_ROLE);
        }
        return userRole;
    }

    private static Long parseUserId(String userIdStr) {
        Long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (Exception e) {
            throw new BaseException(BasicCode.CANNOT_RESOLVE_USER_ID);
        }
        return userId;
    }
}
