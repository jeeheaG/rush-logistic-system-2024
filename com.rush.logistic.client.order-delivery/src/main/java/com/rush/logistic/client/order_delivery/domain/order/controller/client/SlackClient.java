package com.rush.logistic.client.order_delivery.domain.order.controller.client;

import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.request.SendSlackMessageReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("slack-service")
public interface SlackClient {
    @PostMapping("/api/slack")
    SendSlackMessageReq sendSlackMessage(@RequestBody SendSlackMessageReq requestDto);
}
