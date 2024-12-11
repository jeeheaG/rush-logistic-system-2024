package com.rush.logistic.client.company_product.domain.dto.response;

import com.rush.logistic.client.company_product.domain.entity.Product;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductSearchResponse(
        UUID companyId,
        UUID hubId,
        String name,
        Integer quantity
) {
    //entity -> searchDto 새로 생성하는 경우
    public static ProductSearchResponse from(Product product){
        return ProductSearchResponse.builder()
                .companyId(product.getCompanyId())
                .hubId(product.getHubId())
                .name(product.getName())
                .quantity(product.getQuantity())
                .build();
    }
}
