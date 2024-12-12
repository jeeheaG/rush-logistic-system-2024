package com.rush.logistic.client.order_delivery.domain.delivery_route.repository;

import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID> {
}
