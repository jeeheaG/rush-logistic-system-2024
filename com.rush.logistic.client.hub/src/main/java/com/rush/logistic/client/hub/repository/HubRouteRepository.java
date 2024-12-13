package com.rush.logistic.client.hub.repository;

import com.rush.logistic.client.hub.model.HubRoute;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubRouteRepository extends JpaRepository<HubRoute, UUID> {
    Optional<HubRoute> findByStartHubIdAndEndHubId(UUID startHubId, UUID endHubId);

    Optional<Page<HubRoute>> findAllByIsDeleteFalse(Pageable pageable);
}
