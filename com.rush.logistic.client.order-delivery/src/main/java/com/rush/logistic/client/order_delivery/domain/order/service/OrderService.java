package com.rush.logistic.client.order_delivery.domain.order.service;

import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderAllRes;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderCode;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderException;
import com.rush.logistic.client.order_delivery.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderAllRes createOrder(Order order) {
        Order orderSaved = orderRepository.save(order);
        return OrderAllRes.fromEntity(orderSaved);
    }

    public OrderAllRes getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderCode.ORDER_NOT_EXIST));
        return OrderAllRes.fromEntity(order);
    }
}
