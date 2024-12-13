package com.rush.logistic.client.order_delivery.domain.order.service;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryCode;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryException;
import com.rush.logistic.client.order_delivery.domain.delivery.repository.DeliveryRepository;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.delivery_route.repository.DeliveryRouteRepository;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliverymanStatusEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.exception.DeliverymanCode;
import com.rush.logistic.client.order_delivery.domain.deliveryman.exception.DeliverymanException;
import com.rush.logistic.client.order_delivery.domain.deliveryman.repository.DeliverymanRepository;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAllReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAndDeliveryCreateReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderAllRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderUpdateRes;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderCode;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderException;
import com.rush.logistic.client.order_delivery.domain.order.repository.OrderRepository;
import com.rush.logistic.client.order_delivery.domain.order.service.model.HubRouteModel;
import com.rush.logistic.client.order_delivery.domain.order.service.model.StartEndHubIdModel;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {
    private final EntityManager entityManager; // TODO : .saveAndFlush() 로 대체?
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    public OrderAllRes getOrderDetail(UUID orderId) {
        Order order = getOrderEntityById(orderId);
        return OrderAllRes.fromEntity(order);
    }

    @Transactional
    public OrderUpdateRes updateOrder(UUID orderId, OrderAllReq requestDto) {
        Delivery delivery = deliveryRepository.findById(requestDto.deliveryId())
                .orElseThrow(() -> new DeliveryException(DeliveryCode.DELIVERY_NOT_EXIST));

        Order order = getOrderEntityById(orderId);
        order.updateAll(requestDto, delivery);
        entityManager.flush();

        Order orderSaved = getOrderEntityById(orderId);
        return OrderUpdateRes.fromEntity(orderSaved);
    }

    @Transactional
    public UUID deleteOrder(UUID orderId, UUID userId) {
        Order order = getOrderEntityById(orderId);
        order.softDelete(userId.toString());
        return order.getId();
    }

    private Order getOrderEntityById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderCode.ORDER_NOT_EXIST));
    }

}
