package com.rush.logistic.client.company_product.domain.controller;

import com.rush.logistic.client.company_product.domain.dto.CompanyDto;
import com.rush.logistic.client.company_product.domain.dto.request.CompanyCreateRequest;
import com.rush.logistic.client.company_product.domain.dto.request.CompanyUpdateRequest;
import com.rush.logistic.client.company_product.domain.dto.response.CompanySearchResponse;
import com.rush.logistic.client.company_product.domain.service.CompanyService;
import com.rush.logistic.client.company_product.global.exception.Response;
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
    public Response<?> createCompany(@RequestBody CompanyCreateRequest request) {
        CompanyDto result = companyService.createCompany(request);
        return Response.success(result , "업체 생성에 성공하였습니다.");
    }

    @GetMapping
    public Response<Page<CompanyDto>> getAllCompanies(Pageable pageable) {
        Page<CompanyDto> companies = companyService.getAllCompany(pageable);
        return Response.success(companies, "업체 조회에 성공하였습니다.");
    }

    @GetMapping("/{id}")
    public Response<CompanySearchResponse> getCompany(@PathVariable UUID id) {
        return Response.success(companyService.getCompany(id),"업체 단건 조회에 성공하였습니다.");
    }

    @PutMapping("/{id}")
    public Response<?> updateCompany(@PathVariable UUID id, @RequestBody CompanyUpdateRequest request) {
        return Response.success(companyService.updateCompany(id,request), "업체 수정에 성공하였습니다");
    }

    @DeleteMapping("/{id}")
    public Response<?> deleteCompany(@PathVariable UUID id) {
        companyService.deleteCompany(id);
        return Response.success(null,"업체 삭제에 성공하였습니다.");
    }

}
