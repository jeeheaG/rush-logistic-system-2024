package com.rush.logistic.client.company_product.domain.product.dto.request;

import com.rush.logistic.client.company_product.domain.product.dto.ProductDto;

import java.util.UUID;

public record ProductUpdateRequest(
        UUID companyId,
        UUID hubId,
        String name,
        Integer quantity
) {
    public static ProductDto toDto(ProductUpdateRequest request) {
        return ProductDto.builder()
                .companyId(request.companyId())
                .hubId(request.hubId())
                .name(request.name())
                .quantity(request.quantity())
                .build();
    }
}
