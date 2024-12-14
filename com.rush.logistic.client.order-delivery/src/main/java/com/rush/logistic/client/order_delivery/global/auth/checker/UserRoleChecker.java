package com.rush.logistic.client.order_delivery.global.auth.checker;

import com.rush.logistic.client.order_delivery.domain.deliveryman.repository.DeliverymanRepository;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.CompanyClient;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.HubClient;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.UserClient;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.UserSlackResWrapper;
import com.rush.logistic.client.order_delivery.global.auth.UserInfo;
import com.rush.logistic.client.order_delivery.global.auth.UserRole;
import com.rush.logistic.client.order_delivery.global.exception.BaseException;
import com.rush.logistic.client.order_delivery.global.response.BasicCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRoleChecker {
    private final UserClient userClient;
    private final DeliverymanRepository deliverymanRepository;

    public void checkRole(List<RoleOption> options, UserInfo userInfo, UUID inChargeOf) {
        Long userId = userInfo.getUserId();
        GetUserInfoRes userRes = userClient.getUserInfo(userId).data();

        UserRole role = userInfo.getRole();

        isRoleValid(userRes.role(), role);

        boolean isAllowed = false;
        for (RoleOption option : options) {
            if (option.role() == role) {
                if (!option.checkIsInCharge()) {
                    isAllowed = true;
                    break;
                }

                try {
                    switch (option.role()) {
                        case HUB -> {
                            isAllowed = isHubInCharge(userRes, inChargeOf);
                        }
                        case COMPANY -> {
                            isAllowed = isCompanyInCharge(userRes, inChargeOf);
                        }
                        case DELIVERY -> {
                            isAllowed = isDeliveryInCharge(userRes, inChargeOf);
                        }
                    }
                } catch (Exception e) {
                    throw new BaseException(BasicCode.CANNOT_CHECK_USER_ROLE_IN_CHARGE);

                }
            }
        }

        if(!isAllowed) {
            throw new BaseException(BasicCode.USER_NOT_ALLOWED);
        }
    }

    private boolean isDeliveryInCharge(GetUserInfoRes userRes, UUID inChargeOf) {
        // delivery 엔티티 조회해서 담당 배달인지 조회
        return false;
    }

    private boolean isCompanyInCharge(GetUserInfoRes userRes, UUID inChargeOf) {
        // 사용자 조회해서 담당 업체인지 조회
        return false;
    }

    private boolean isHubInCharge(GetUserInfoRes userRes, UUID inChargeOf) {
        // 사용자 조회해서 담당 허브인지 조회
        return false;
    }

    private void isRoleValid(UserRole newRole, UserRole checkRole) {
        if (!newRole.equals(checkRole)) {
            throw new BaseException(BasicCode.TOKEN_USER_ROLE_NOT_VALID);
        }
    }

}
