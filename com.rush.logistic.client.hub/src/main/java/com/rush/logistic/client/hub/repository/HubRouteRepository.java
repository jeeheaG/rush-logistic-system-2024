package com.rush.logistic.client.hub.repository;

import com.rush.logistic.client.hub.model.HubRoute;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubRouteRepository extends JpaRepository<HubRoute, UUID> {
}
