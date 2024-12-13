package com.rush.logistic.client.company_product.domain.company.dto.request;

import com.rush.logistic.client.company_product.domain.company.dto.CompanyDto;
import com.rush.logistic.client.company_product.global.type.CompanyType;

import java.util.UUID;

public record CompanyUpdateRequest(
        UUID hubId,
        String name,
        String address,
        CompanyType type

) {
    public static CompanyDto toDto(CompanyUpdateRequest request) {
        return CompanyDto.builder()
                .hubId(request.hubId())
                .name(request.name())
                .address(request.address())
                .type(request.type())
                .build();
    }
}
