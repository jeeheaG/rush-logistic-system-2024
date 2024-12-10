package com.rush.logistic.client.company_product.domain.dto;

import com.rush.logistic.client.company_product.domain.entity.Company;
import com.rush.logistic.client.company_product.domain.type.CompanyType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CompanyDto(
        UUID id,
        UUID hubId,
        String name,
        String address,
        CompanyType type,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy,
        Boolean isDelete
) {
    //dto -> entity
    public Company toEntity(CompanyDto dto) {
        Company company = Company.builder()
                .hubId(hubId)
                .name(name)
                .address(address)
                .type(type)
                .build();

        return company;
    }

    //entity -> dto
    public static CompanyDto from(Company entity) {
        return CompanyDto.builder()
                .id(entity.getId())
                .hubId(entity.getHubId())
                .name(entity.getName())
                .address(entity.getAddress())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .isDelete(entity.getIsDelete())
                .build();
    }
}
