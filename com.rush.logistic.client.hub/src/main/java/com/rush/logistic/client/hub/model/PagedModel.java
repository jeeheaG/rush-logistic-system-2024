package com.rush.logistic.client.hub.model;

import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

public class PagedModel<T> {
    private final Page<T> page;

    public PagedModel(Page<T> page){
        Assert.notNull(page, "Page must not be null");

        this.page = page;
    }
}
