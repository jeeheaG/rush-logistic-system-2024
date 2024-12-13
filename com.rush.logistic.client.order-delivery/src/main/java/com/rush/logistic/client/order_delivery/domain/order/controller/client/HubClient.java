package com.rush.logistic.client.order_delivery.domain.order.controller.client;

import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.request.HubRouteInfoReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.HubRouteInfoRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("hub-service")
public interface HubClient {
    @PostMapping("/api/hubs-routes")
    HubRouteInfoRes getHubRoute(@RequestBody HubRouteInfoReq requestDto);
}
