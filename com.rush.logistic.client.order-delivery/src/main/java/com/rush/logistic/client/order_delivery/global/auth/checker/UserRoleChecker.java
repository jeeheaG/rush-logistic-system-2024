package com.rush.logistic.client.order_delivery.global.auth.checker;

import com.rush.logistic.client.order_delivery.domain.order.controller.client.UserClient;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import com.rush.logistic.client.order_delivery.global.auth.UserInfo;
import com.rush.logistic.client.order_delivery.global.auth.UserRole;
import com.rush.logistic.client.order_delivery.global.exception.BaseException;
import com.rush.logistic.client.order_delivery.global.response.BasicCode;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public abstract class UserRoleChecker<T> {
    private final UserClient userClient;

    public GetUserInfoRes getUserAndCheckAllRole(UserInfo userInfo) {
        return this.getUserAndCheckRole(getAllRoles(), userInfo);
    }

    /**
     * 권한의 현재 유효성 확인, 허용 권한에 포함되어 있는지 확인
     * @param allowedRoles
     * @param userInfo
     * @return
     */
    public GetUserInfoRes getUserAndCheckRole(List<UserRole> allowedRoles, UserInfo userInfo) {
        Long userId = userInfo.getUserId();
        GetUserInfoRes userRes = userClient.getUserInfo(userId).data();
        UserRole role = userInfo.getRole();

        isRoleValid(userRes.role(), role);

        if(allowedRoles.contains(role)) {
            return userRes;
        }
        throw new BaseException(BasicCode.USER_NOT_ALLOWED);
    }

    public abstract void checkInCharge(T target, GetUserInfoRes getUserInfoRes);


    private void isRoleValid(UserRole newRole, UserRole checkRole) {
        if (!newRole.equals(checkRole)) {
            throw new BaseException(BasicCode.TOKEN_USER_ROLE_NOT_VALID);
        }
    }

    private List<UserRole> getAllRoles() {
        return Arrays.asList(UserRole.MASTER, UserRole.HUB, UserRole.COMPANY, UserRole.DELIVERY);
    }
}
