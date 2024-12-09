package com.rush.logistic.client.company_product.domain.controller;

import com.rush.logistic.client.company_product.domain.dto.request.CompanyCreateRequest;
import com.rush.logistic.client.company_product.domain.service.CompanyService;
import com.rush.logistic.client.company_product.global.exception.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    @Value("${server.port}")
    private String port;

    private final CompanyService companyService;

    @PostMapping
    public Response<?> company(@RequestBody CompanyCreateRequest request) {
        companyService.createCompany(request);
        return Response.success(request , "업체 생성에 성공하였습니다.");
    }

}
