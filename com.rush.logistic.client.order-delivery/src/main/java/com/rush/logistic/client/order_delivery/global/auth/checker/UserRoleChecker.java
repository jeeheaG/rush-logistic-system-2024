package com.rush.logistic.client.order_delivery.global.auth.checker;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryCode;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryException;
import com.rush.logistic.client.order_delivery.domain.delivery.repository.DeliveryRepository;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.delivery_route.repository.DeliveryRouteRepository;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.UserClient;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import com.rush.logistic.client.order_delivery.global.auth.UserInfo;
import com.rush.logistic.client.order_delivery.global.auth.UserRole;
import com.rush.logistic.client.order_delivery.global.exception.BaseException;
import com.rush.logistic.client.order_delivery.global.response.BasicCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRoleChecker {
    private final UserClient userClient;
    private final DeliveryRouteRepository deliveryRouteRepository;

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

    /**
     * order 도메인에서 해당 리소스에 담당 권한이 있는지 확인하는 메서드
     * @param order
     * @param getUserInfoRes
     */
    public void checkInCharge(Order order, GetUserInfoRes getUserInfoRes) {
        UserRole role = getUserInfoRes.role();

        switch (role) {
            case HUB -> checkHubInCharge(order, getUserInfoRes.hubId());
            case COMPANY -> checkCompanyInCharge(order, getUserInfoRes.companyId());
            case DELIVERY -> checkDeliveryInCharge(order, getUserInfoRes.userId());
        }
    }

    /**
     * 주문의 출발, 최종 도착 허브 담당자만 허용
     * @param order
     * @param userHubId
     */
    private void checkHubInCharge(Order order, UUID userHubId) {
        UUID startHubId = order.getDelivery().getStartHubId();
        UUID endHubId = order.getDelivery().getEndHubId();
        if (!startHubId.equals(userHubId) && !endHubId.equals(userHubId)) {
            throw new BaseException(BasicCode.USER_NOT_ALLOWED_HUB);
        }
    }

    /**
     * 주문의 생산, 수령 업체 담당자만 허용
     * @param order
     * @param userCompanyId
     */
    private void checkCompanyInCharge(Order order, UUID userCompanyId) {
        UUID produceCompanyId = order.getProduceCompanyId();
        UUID receiveCompanyId = order.getReceiveCompanyId();
        if (!produceCompanyId.equals(userCompanyId) && !receiveCompanyId.equals(userCompanyId) ) {
            throw new BaseException(BasicCode.USER_NOT_ALLOWED_COMPANY);
        }
    }

    private void checkDeliveryInCharge(Order order, Long userId) {
        Delivery delivery = order.getDelivery();
        // delivery 엔티티 조회해서 담당 배달경로를 포함하는 배달인지 조회

        List<DeliveryRoute> deliveryRoutes = deliveryRouteRepository.findAllByDelivery(delivery);
        if (deliveryRoutes==null || deliveryRoutes.isEmpty()){
            throw new DeliveryException(DeliveryCode.DELIVERY_HAS_NO_ROUTE);
        }
        else {
            // 배달의 모든 배달 경로의 배달 담당자 확인
            for (DeliveryRoute route : deliveryRoutes) {
                Long deliverymanUserId = route.getDeliveryman().getUserId();
                if (deliverymanUserId.equals(userId)) {
                    return;
                }
            }
            throw new BaseException(BasicCode.USER_NOT_ALLOWED_DELIVERY);
        }
    }

    private void isRoleValid(UserRole newRole, UserRole checkRole) {
        if (!newRole.equals(checkRole)) {
            throw new BaseException(BasicCode.TOKEN_USER_ROLE_NOT_VALID);
        }
    }

    private List<UserRole> getAllRoles() {
        return Arrays.asList(UserRole.MASTER, UserRole.HUB, UserRole.COMPANY, UserRole.DELIVERY);
    }

}
