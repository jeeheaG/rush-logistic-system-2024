package com.rush.logistic.client.order_delivery.domain.order.controller;

import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.CreateOrderReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderAllRes;
import com.rush.logistic.client.order_delivery.domain.order.service.OrderService;
import com.rush.logistic.client.order_delivery.global.response.BaseResponse;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody CreateOrderReq requestDto) {
        log.info("OrderController createOrder");

        OrderAllRes responseDto = orderService.createOrder(requestDto.toEntity());
        return ResponseEntity.ok().body(BaseResponse.toResponse(OrderCode.CREATE_ORDER_OK, responseDto));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable UUID orderId) {
        log.info("OrderController getOrderById");

        OrderAllRes responseDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok().body(BaseResponse.toResponse(OrderCode.GET_ORDER_OK, responseDto));
    }
}
