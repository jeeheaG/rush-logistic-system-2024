package com.rush.logistic.client.order_delivery.global.auth.checker;

import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.UserClient;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.global.auth.UserRole;
import com.rush.logistic.client.order_delivery.global.exception.BaseException;
import com.rush.logistic.client.order_delivery.global.response.BasicCode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeliverymanUserRoleChecker extends UserRoleChecker<Deliveryman> {

    public DeliverymanUserRoleChecker(UserClient userClient) {
        super(userClient);
    }

    /**
     * deliveryman 도메인에서 해당 리소스에 담당 권한이 있는지 확인하는 메서드
     * @param deliveryman
     * @param getUserInfoRes
     */
    public void checkInCharge(Deliveryman deliveryman, GetUserInfoRes getUserInfoRes) {
        UserRole role = getUserInfoRes.role();

        switch (role) {
            case HUB -> checkHubInCharge(deliveryman, getUserInfoRes.hubId());
//            case COMPANY -> checkCompanyInCharge(deliveryman, getUserInfoRes.companyId()); // 해당 권한 명시된 바 없음
            case DELIVERY -> checkDeliveryInCharge(deliveryman, getUserInfoRes.userId());
        }
    }

    /**
     * 담당 허브만 허용
     * @param deliveryman
     * @param userHubId
     */
    private void checkHubInCharge(Deliveryman deliveryman, UUID userHubId) {
        UUID inChargeHubId = deliveryman.getHubInChargeId();
        if (!inChargeHubId.equals(userHubId)) {
            throw new BaseException(BasicCode.USER_NOT_ALLOWED_HUB);
        }
    }

    /**
     * 배달경로 담당자만 허용
     * @param deliveryman
     * @param userId
     */
    private void checkDeliveryInCharge(Deliveryman deliveryman, Long userId) {
        if (!deliveryman.getUserId().equals(userId)) {
            throw new BaseException(BasicCode.USER_NOT_ALLOWED_DELIVERYMAN);
        }
    }


}
