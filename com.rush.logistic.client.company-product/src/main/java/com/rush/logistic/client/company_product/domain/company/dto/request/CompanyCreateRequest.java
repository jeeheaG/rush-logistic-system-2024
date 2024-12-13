package com.rush.logistic.client.company_product.domain.company.dto.request;

import com.rush.logistic.client.company_product.domain.company.dto.CompanyDto;
import com.rush.logistic.client.company_product.global.type.CompanyType;

import java.util.UUID;

public record CompanyCreateRequest (
        UUID id,
        UUID hubId,
        String name,
        String address,
        CompanyType type

){
    //request -> dto
    public static CompanyDto toDto(CompanyCreateRequest request) {
        return CompanyDto.builder()
                .id(request.id)
                .hubId(request.hubId())
                .name(request.name())
                .address(request.address())
                .type(request.type())
                .build();
    }
}
