package com.rush.logistic.client.company_product.domain.dto;

import com.rush.logistic.client.company_product.domain.entity.Product;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ProductDto(
        UUID id,
        UUID companyId,
        UUID hubId,
        String name,
        Integer quantity,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy,
        Boolean isDelete
) {
    //dto -> entity
    public Product toEntity(ProductDto dto){
        Product product = Product.builder()
                .companyId(companyId)
                .hubId(hubId)
                .name(name)
                .quantity(quantity)
                .build();
        return product;
    }

    //entity -> dto
    public static ProductDto from(Product product){
        return ProductDto.builder()
                .id(product.getId())
                .companyId(product.getCompanyId())
                .hubId(product.getHubId())
                .name(product.getName())
                .quantity(product.getQuantity())
                .createdAt(product.getCreatedAt())
                .createdBy(product.getCreatedBy())
                .updatedAt(product.getUpdatedAt())
                .updatedBy(product.getUpdatedBy())
                .deletedAt(product.getDeletedAt())
                .deletedBy(product.getDeletedBy())
                .isDelete(product.getIsDelete())
                .build();
    }
}
