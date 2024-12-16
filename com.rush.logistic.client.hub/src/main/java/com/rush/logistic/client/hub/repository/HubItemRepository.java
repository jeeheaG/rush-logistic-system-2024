package com.rush.logistic.client.hub.repository;

import com.rush.logistic.client.hub.model.HubItem;
import org.springframework.data.repository.CrudRepository;

public interface HubItemRepository extends CrudRepository<HubItem, String> {
    boolean existsByName(String name);

    HubItem findByName(String name);

    boolean existsByAddress(String address);

    HubItem findByAddress(String name);
}
