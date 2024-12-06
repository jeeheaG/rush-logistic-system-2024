package com.rush.logistic.client.order_delivery.domain.checker;

import com.rush.logistic.client.order_delivery.global.common.response.BaseResponse;
import com.rush.logistic.client.order_delivery.global.common.response.BasicCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class HealthCheckController {
    @GetMapping("/health-check")
    public ResponseEntity<Object> healthChecker(){
        return ResponseEntity.ok().body(BaseResponse.toResponse(BasicCode.TEST_OK, "health check OK"));
    }
}
