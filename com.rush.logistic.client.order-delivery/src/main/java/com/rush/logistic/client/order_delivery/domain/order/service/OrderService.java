package com.rush.logistic.client.order_delivery.domain.order.service;

import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAllReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderAllRes;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderCode;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderException;
import com.rush.logistic.client.order_delivery.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public OrderAllRes createOrder(Order order) {
        Order orderSaved = orderRepository.save(order);
        return OrderAllRes.fromEntity(orderSaved);
    }

    public OrderAllRes getOrderDetail(UUID orderId) {
        Order order = getOrderEntityById(orderId);
        return OrderAllRes.fromEntity(order);
    }

    @Transactional
    public OrderAllRes updateOrder(UUID orderId, OrderAllReq requestDto) {
        Order order = getOrderEntityById(orderId);
        order.updateAll(requestDto);
        return OrderAllRes.fromEntity(order);
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
