package com.rush.logistic.client.order_delivery.global.auth.checker;

import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.UserClient;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderCode;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderException;
import com.rush.logistic.client.order_delivery.domain.order.repository.OrderRepository;
import com.rush.logistic.client.order_delivery.global.auth.UserRole;
import com.rush.logistic.client.order_delivery.global.exception.BaseException;
import com.rush.logistic.client.order_delivery.global.response.BasicCode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeliveryRouteUserRoleChecker extends UserRoleChecker<DeliveryRoute> {
    private final OrderRepository orderRepository;

    public DeliveryRouteUserRoleChecker(UserClient userClient, OrderRepository orderRepository) {
        super(userClient);
        this.orderRepository = orderRepository;
    }

    /**
     * deliveryRoute 도메인에서 해당 리소스에 담당 권한이 있는지 확인하는 메서드
     * @param deliveryRoute
     * @param getUserInfoRes
     */
    public void checkInCharge(DeliveryRoute deliveryRoute, GetUserInfoRes getUserInfoRes) {
        UserRole role = getUserInfoRes.role();

        switch (role) {
            case HUB -> checkHubInCharge(deliveryRoute, getUserInfoRes.hubId());
//            case COMPANY -> checkCompanyInCharge(deliveryRoute, getUserInfoRes.companyId()); // 업체 담당자 모두 허용인듯?
            case DELIVERY -> checkDeliveryInCharge(deliveryRoute, getUserInfoRes.userId());
        }
    }

    /**
     * 배달 경로의 출발, 도착 허브 담당자만 허용
     * @param deliveryRoute
     * @param userHubId
     */
    private void checkHubInCharge(DeliveryRoute deliveryRoute, UUID userHubId) {
        UUID startHubId = deliveryRoute.getStartHubId();
        UUID endHubId = deliveryRoute.getEndHubId();
        if (!startHubId.equals(userHubId) && !endHubId.equals(userHubId)) {
            throw new BaseException(BasicCode.USER_NOT_ALLOWED_HUB);
        }
    }

    /**
     * 생산, 수령 업체 담당자만 허용
     * @param deliveryRoute
     * @param userCompanyId
     */
    private void checkCompanyInCharge(DeliveryRoute deliveryRoute, UUID userCompanyId) {
        Order order = orderRepository.findByDelivery(deliveryRoute.getDelivery())
                .orElseThrow(() -> new OrderException(OrderCode.ORDER_NOT_EXIST));

        UUID produceCompanyId = order.getProduceCompanyId();
        UUID receiveCompanyId = order.getReceiveCompanyId();
        if (!produceCompanyId.equals(userCompanyId) && !receiveCompanyId.equals(userCompanyId) ) {
            throw new BaseException(BasicCode.USER_NOT_ALLOWED_COMPANY);
        }
    }

    /**
     * 배달경로 담당자만 허용
     * @param deliveryRoute
     * @param userId
     */
    private void checkDeliveryInCharge(DeliveryRoute deliveryRoute, Long userId) {
        Long deliverymanUserId = deliveryRoute.getDeliveryman().getUserId();
        if (!deliverymanUserId.equals(userId)) {
            throw new BaseException(BasicCode.USER_NOT_ALLOWED_DELIVERY_ROUTE);
        }
    }


}
