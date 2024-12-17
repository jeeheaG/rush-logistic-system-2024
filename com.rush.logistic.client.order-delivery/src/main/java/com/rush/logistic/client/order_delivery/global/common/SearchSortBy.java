package com.rush.logistic.client.order_delivery.global.common;

import lombok.Getter;

@Getter
public enum SearchSortBy {
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt");

    SearchSortBy(String fieldName) {
        this.fieldName = fieldName;
    }

    private final String fieldName;
}
