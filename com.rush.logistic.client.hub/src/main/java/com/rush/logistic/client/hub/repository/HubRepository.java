package com.rush.logistic.client.hub.repository;

import com.rush.logistic.client.hub.model.Hub;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubRepository extends JpaRepository<Hub, UUID> {
    Optional<Page<Hub>> findPagedByIsDeleteFalse(Pageable pageable);
    Optional<List<Hub>> findAllAsListByIsDeleteFalse();

    boolean existsByAddress(String address);

    boolean existsByHubName(String name);

    Hub findByName(String name);

    Hub findByAddress(String name);
}
