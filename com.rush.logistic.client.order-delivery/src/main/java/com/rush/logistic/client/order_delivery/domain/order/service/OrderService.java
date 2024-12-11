package com.rush.logistic.client.order_delivery.domain.order.service;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryCode;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryException;
import com.rush.logistic.client.order_delivery.domain.delivery.repository.DeliveryRepository;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAllReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAndDeliveryAllReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderAllRes;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderCode;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderException;
import com.rush.logistic.client.order_delivery.domain.order.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final EntityManager entityManager;

    @Transactional
    public OrderAllRes createDeliveryAndOrder(OrderAndDeliveryAllReq requestDto) {
        Delivery delivery = requestDto.deliveryInfo().toEntity();
        Delivery deliverySaved = deliveryRepository.save(delivery);

        Order order = requestDto.toEntity(deliverySaved);
        Order orderSaved = orderRepository.save(order);
        return OrderAllRes.fromEntity(orderSaved);
    }

    public OrderAllRes getOrderDetail(UUID orderId) {
        Order order = getOrderEntityById(orderId);
        return OrderAllRes.fromEntity(order);
    }

    @Transactional
    public OrderAllRes updateOrder(UUID orderId, OrderAllReq requestDto) {
        Delivery delivery = deliveryRepository.findById(requestDto.deliveryId())
                .orElseThrow(() -> new DeliveryException(DeliveryCode.DELIVERY_NOT_EXIST));

        Order order = getOrderEntityById(orderId);
        order.updateAll(requestDto, delivery);
        entityManager.flush();

        Order orderSaved = getOrderEntityById(orderId);
        return OrderAllRes.fromEntity(orderSaved);
    }

    @Transactional
    public UUID deleteOrder(UUID orderId, UUID userId) {
        Order order = getOrderEntityById(orderId);
        order.softDelete(userId);
        return order.getId();
    }


    private Order getOrderEntityById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderCode.ORDER_NOT_EXIST));
    }

}
