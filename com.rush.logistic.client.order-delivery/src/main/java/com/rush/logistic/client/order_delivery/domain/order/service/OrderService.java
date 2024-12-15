package com.rush.logistic.client.order_delivery.domain.order.service;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryCode;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryException;
import com.rush.logistic.client.order_delivery.domain.delivery.repository.DeliveryRepository;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.delivery_route.repository.DeliveryRouteRepository;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAllReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderAllRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderUpdateRes;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderCode;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderException;
import com.rush.logistic.client.order_delivery.domain.order.repository.OrderRepository;
import com.rush.logistic.client.order_delivery.global.auth.checker.UserRoleChecker;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {
    private final EntityManager entityManager; // TODO : .saveAndFlush() 로 대체?
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryRouteRepository deliveryRouteRepository;
    private final UserRoleChecker userRoleChecker;

    public OrderAllRes getOrderDetail(UUID orderId, GetUserInfoRes getUserInfoRes) {
        Order order = getOrderEntityById(orderId);
        userRoleChecker.checkInCharge(order, getUserInfoRes);

        List<DeliveryRoute> deliveryRoutes = deliveryRouteRepository.findAllByDelivery(order.getDelivery());
        return OrderAllRes.fromEntity(order, deliveryRoutes);
    }

    @Transactional
    public OrderUpdateRes updateOrder(UUID orderId, OrderAllReq requestDto, GetUserInfoRes getUserInfoRes) {
        Delivery delivery = deliveryRepository.findById(requestDto.deliveryId())
                .orElseThrow(() -> new DeliveryException(DeliveryCode.DELIVERY_NOT_EXIST));

        Order order = getOrderEntityById(orderId);
        userRoleChecker.checkInCharge(order, getUserInfoRes);

        order.updateAll(requestDto, delivery);
        entityManager.flush();

        Order orderSaved = getOrderEntityById(orderId);
        return OrderUpdateRes.fromEntity(orderSaved);
    }

    @Transactional
    public UUID deleteOrder(UUID orderId, Long userId, GetUserInfoRes getUserInfoRes) {
        Order order = getOrderEntityById(orderId);
        userRoleChecker.checkInCharge(order, getUserInfoRes);

        order.softDelete(userId.toString());
        return order.getId();
    }

    private Order getOrderEntityById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderCode.ORDER_NOT_EXIST));
    }

}
