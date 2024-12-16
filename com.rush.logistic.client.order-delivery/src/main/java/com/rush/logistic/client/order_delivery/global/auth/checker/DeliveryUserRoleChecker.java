package com.rush.logistic.client.order_delivery.global.auth.checker;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryCode;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryException;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.delivery_route.repository.DeliveryRouteRepository;
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

import java.util.List;
import java.util.UUID;

@Component
public class DeliveryUserRoleChecker extends UserRoleChecker<Delivery> {
    private final OrderRepository orderRepository;
    private final DeliveryRouteRepository deliveryRouteRepository;

    public DeliveryUserRoleChecker(UserClient userClient, OrderRepository orderRepository, DeliveryRouteRepository deliveryRouteRepository) {
        super(userClient);
        this.orderRepository = orderRepository;
        this.deliveryRouteRepository = deliveryRouteRepository;
    }

    /**
     * delivery 도메인에서 해당 리소스에 담당 권한이 있는지 확인하는 메서드
     * @param delivery
     * @param getUserInfoRes
     */
    public void checkInCharge(Delivery delivery, GetUserInfoRes getUserInfoRes) {
        UserRole role = getUserInfoRes.role();

        switch (role) {
            case HUB -> checkHubInCharge(delivery, getUserInfoRes.hubId());
//            case COMPANY -> checkCompanyInCharge(delivery, getUserInfoRes.companyId()); // 업체 담당자 모두 허용인듯?
            case DELIVERY -> checkDeliveryInCharge(delivery, getUserInfoRes.userId());
        }
    }

    /**
     * 배달의 출발, 도착 허브 담당자만 허용
     * @param delivery
     * @param userHubId
     */
    private void checkHubInCharge(Delivery delivery, UUID userHubId) {
        UUID startHubId = delivery.getStartHubId();
        UUID endHubId = delivery.getEndHubId();
        if (!startHubId.equals(userHubId) && !endHubId.equals(userHubId)) {
            throw new BaseException(BasicCode.USER_NOT_ALLOWED_HUB);
        }
    }

    /**
     * 생산, 수령 업체 담당자만 허용
     * @param delivery
     * @param userCompanyId
     */
    private void checkCompanyInCharge(Delivery delivery, UUID userCompanyId) {
        Order order = orderRepository.findByDelivery(delivery)
                .orElseThrow(() -> new OrderException(OrderCode.ORDER_NOT_EXIST));

        UUID produceCompanyId = order.getProduceCompanyId();
        UUID receiveCompanyId = order.getReceiveCompanyId();
        if (!produceCompanyId.equals(userCompanyId) && !receiveCompanyId.equals(userCompanyId) ) {
            throw new BaseException(BasicCode.USER_NOT_ALLOWED_COMPANY);
        }
    }

    /**
     * 배달에 포함된 배달경로의 담당자만 허용
     * @param delivery
     * @param userId
     */
    private void checkDeliveryInCharge(Delivery delivery, Long userId) {
        // delivery 가 담당 배달경로를 포함하는 배달인지 조회

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


}
