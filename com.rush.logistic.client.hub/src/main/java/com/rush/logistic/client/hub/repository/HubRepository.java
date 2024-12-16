package com.rush.logistic.client.hub.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.rush.logistic.client.hub.model.Hub;
import com.rush.logistic.client.hub.model.QHub;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface HubRepository extends JpaRepository<Hub, UUID>,
        HubRepositoryCustom,
        QuerydslPredicateExecutor<Hub>,
        QuerydslBinderCustomizer<QHub> {
    Optional<Page<Hub>> findPagedByIsDeleteFalse(Pageable pageable);
    Optional<List<Hub>> findAllAsListByIsDeleteFalse();

    boolean existsByAddressAndIsDeleteFalse(String address);

    boolean existsByNameAndIsDeleteFalse(String name);

    Hub findByNameAndIsDeleteFalse(String name);

    Hub findByAddressAndIsDeleteFalse(String name);

    @Override
    default void customize(QuerydslBindings querydslBindings, @NotNull QHub qHub) {
        querydslBindings.bind(String.class).all((StringPath path, Collection<? extends String> values) -> {
            List<String> valuesList = new ArrayList<>(values.stream().map(String::trim).toList());
            if(valuesList.isEmpty()){
                return Optional.empty();
            }
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String s : valuesList) {
                booleanBuilder.or(path.containsIgnoreCase(s));
            }
            return Optional.of(booleanBuilder);
        });
    }
}
