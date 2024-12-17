package com.rush.logistic.client.order_delivery.domain.order.controller.client;

import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.request.GetStartEndHubIdOfCompanyReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.CompanyResWrapper;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetCompanyByIdRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetStartEndHubIdOfCompanyRes;
import com.rush.logistic.client.order_delivery.global.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(value = "company-product-service", configuration = FeignConfig.class)
public interface CompanyClient {
    @PostMapping("/api/companies/mapping")
    CompanyResWrapper<GetStartEndHubIdOfCompanyRes> getStartEndHubIdOfCompany(@RequestBody GetStartEndHubIdOfCompanyReq requestDto);

    @GetMapping("/api/companies/{companyId}")
    CompanyResWrapper<GetCompanyByIdRes> getCompanyById(@PathVariable UUID companyId);
}
