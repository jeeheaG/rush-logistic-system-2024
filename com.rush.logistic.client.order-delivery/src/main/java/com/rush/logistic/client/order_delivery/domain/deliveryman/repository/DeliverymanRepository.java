package com.rush.logistic.client.order_delivery.domain.deliveryman.repository;

import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliverymanStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliverymanRepository extends JpaRepository<Deliveryman, UUID>, DeliverymanRepositoryCustom {
    Optional<Deliveryman> findByUserId(Long userId);

    Optional<Deliveryman> findTop1ByLastHubIdAndStatusOrderByLastDeliveryTimeAsc(UUID startHubId, DeliverymanStatusEnum status);
    Optional<Deliveryman> findTop1ByHubInChargeIdAndStatusOrderByLastDeliveryTimeAsc(UUID startHubId, DeliverymanStatusEnum status);
}
