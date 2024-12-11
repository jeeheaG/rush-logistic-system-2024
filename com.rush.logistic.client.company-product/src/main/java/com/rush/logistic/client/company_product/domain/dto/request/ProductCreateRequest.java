package com.rush.logistic.client.company_product.domain.dto.request;

import com.rush.logistic.client.company_product.domain.dto.ProductDto;

import java.util.UUID;

public record ProductCreateRequest(
        UUID id,
        UUID companyId,
        UUID hubId,
        String name,
        Integer quantity
) {
    //request -> dto
    public static ProductDto toDto(ProductCreateRequest request) {
        return ProductDto.builder()
                .id(request.id)
                .companyId(request.companyId)
                .hubId(request.hubId)
                .name(request.name)
                .quantity(request.quantity)
                .build();
    }
}
