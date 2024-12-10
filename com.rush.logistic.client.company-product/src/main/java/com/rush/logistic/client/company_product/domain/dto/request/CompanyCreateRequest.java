package com.rush.logistic.client.company_product.domain.dto.request;

import com.rush.logistic.client.company_product.domain.dto.CompanyDto;
import com.rush.logistic.client.company_product.domain.type.CompanyType;

import java.util.UUID;

public record CompanyCreateRequest (
        UUID hubId,
        String name,
        String address,
        CompanyType type

){
    //request -> dto
    public static CompanyDto toDto(CompanyCreateRequest request) {
        return CompanyDto.builder()
                .hubId(request.hubId())
                .name(request.name())
                .address(request.address())
                .type(request.type())
                .build();
    }
}
