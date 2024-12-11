package com.rush.logistic.client.order_delivery.domain.delivery.repository;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
}
