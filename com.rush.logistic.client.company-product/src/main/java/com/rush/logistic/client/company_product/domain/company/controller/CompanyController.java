package com.rush.logistic.client.company_product.domain.company.controller;

import com.rush.logistic.client.company_product.domain.company.dto.CompanyDto;
import com.rush.logistic.client.company_product.domain.company.dto.request.CompanyCreateRequest;
import com.rush.logistic.client.company_product.domain.company.dto.request.CompanyHubMappingRequest;
import com.rush.logistic.client.company_product.domain.company.dto.request.CompanyUpdateRequest;
import com.rush.logistic.client.company_product.domain.company.dto.response.CompanyHubMappingResponse;
import com.rush.logistic.client.company_product.domain.company.dto.response.CompanySearchResponse;
import com.rush.logistic.client.company_product.domain.company.service.CompanyService;
import com.rush.logistic.client.company_product.global.exception.Response;
import com.rush.logistic.client.company_product.global.type.CompanyType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    @Value("${server.port}")
    private String port;

    private final CompanyService companyService;

    @PostMapping
    public Response<?> createCompany(@RequestBody CompanyCreateRequest request,
                                     @RequestHeader(value = "role", required = true) String role,
                                     @RequestHeader(value = "USER_ID", required = true) String authenticatedUserId
    ) {
        CompanyDto result = companyService.createCompany(request,role,authenticatedUserId);
        return Response.success(result, "업체 생성에 성공하였습니다.");
    }

    @PostMapping("/mapping")
    public Response<CompanyHubMappingResponse> getCompanyHubMapping(
            @RequestBody CompanyHubMappingRequest request
    ) {
        CompanyHubMappingResponse response = companyService.getCompanyHubMapping(request);
        return Response.success(response, "업체 허브 매핑 조회에 성공하였습니다.");
    }

    @GetMapping
    public Response<Page<CompanyDto>> getAllCompanies(String name,
                                                      UUID hubId,
                                                      CompanyType companyType,
                                                      Pageable pageable,
                                                      String sortType
    ) {
        Page<CompanyDto> companies = companyService.getAllCompany(name, hubId, companyType, pageable, sortType);

        return Response.success(companies, "업체 조회에 성공하였습니다.");
    }

    @GetMapping("/{id}")
    public Response<CompanySearchResponse> getCompany(@PathVariable UUID id) {
        return Response.success(companyService.getCompany(id), "업체 단건 조회에 성공하였습니다.");
    }

    @PutMapping("/{id}")
    public Response<?> updateCompany(@PathVariable UUID id, @RequestBody CompanyUpdateRequest request,
                                     @RequestHeader(value = "role", required = true) String role,
                                     @RequestHeader(value = "USER_ID", required = true) String authenticatedUserId) {
        return Response.success(companyService.updateCompany(id, request, role, authenticatedUserId), "업체 수정에 성공하였습니다");
    }

    @DeleteMapping("/{id}")
    public Response<?> deleteCompany(@PathVariable UUID id,
                                     @RequestHeader(value = "role", required = true) String role,
                                     @RequestHeader(value = "USER_ID", required = true) String authenticatedUserId
    ) {
        companyService.deleteCompany(id,role,authenticatedUserId);
        return Response.success(null, "업체 삭제에 성공하였습니다.");
    }

}
