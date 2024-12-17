package com.rush.logistic.client.hub.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import com.rush.logistic.client.hub.model.HubRoute;
import com.rush.logistic.client.hub.model.QHubRoute;
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

public interface HubRouteRepository extends JpaRepository<HubRoute, UUID>,
        HubRouteRepositoryCustom,
        QuerydslPredicateExecutor<HubRoute>,
        QuerydslBinderCustomizer<QHubRoute> {
    Optional<HubRoute> findByStartHubIdAndEndHubId(UUID startHubId, UUID endHubId);

    Optional<Page<HubRoute>> findAllByIsDeleteFalse(Pageable pageable);

    Optional<HubRoute> findByStartHubIdAndEndHubIdAndIsDeleteFalse(UUID startHubId, UUID endHubId);

    Page<HubRoute> findAll(Predicate predicate, Pageable pageable);

    @Override
    default void customize(QuerydslBindings querydslBindings, @NotNull QHubRoute qHubRoute) {
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
