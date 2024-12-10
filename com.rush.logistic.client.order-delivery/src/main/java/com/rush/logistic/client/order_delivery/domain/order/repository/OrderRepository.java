package com.rush.logistic.client.order_delivery.domain.order.repository;

import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
