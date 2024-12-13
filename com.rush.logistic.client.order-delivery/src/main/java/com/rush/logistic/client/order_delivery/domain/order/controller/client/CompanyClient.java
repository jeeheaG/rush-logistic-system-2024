package com.rush.logistic.client.order_delivery.domain.order.controller.client;

import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.request.GetStartEndHubIdOfCompanyReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetStartEndHubIdOfCompanyResWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("company-service")
public interface CompanyClient {
    @PostMapping("/api/companies")
    GetStartEndHubIdOfCompanyResWrapper getStartEndHubIdOfCompany(@RequestBody GetStartEndHubIdOfCompanyReq requestDto);
}
