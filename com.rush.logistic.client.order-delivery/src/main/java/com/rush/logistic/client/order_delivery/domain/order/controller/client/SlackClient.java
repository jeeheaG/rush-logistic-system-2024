package com.rush.logistic.client.order_delivery.domain.order.controller.client;

import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.request.SendSlackMessageReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.SendSlackMessageRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.UserSlackResWrapper;
import com.rush.logistic.client.order_delivery.global.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "slack", configuration = FeignConfig.class)
public interface SlackClient {
    @PostMapping("/api/slacks")
    UserSlackResWrapper<SendSlackMessageRes> sendSlackMessage(@RequestBody SendSlackMessageReq requestDto);
}
