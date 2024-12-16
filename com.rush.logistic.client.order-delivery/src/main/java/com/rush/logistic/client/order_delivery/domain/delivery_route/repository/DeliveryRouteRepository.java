package com.rush.logistic.client.order_delivery.domain.delivery_route.repository;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID> {
    List<DeliveryRoute> findAllByDelivery(Delivery delivery);
}
