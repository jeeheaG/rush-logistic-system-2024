package com.rush.logistic.client.order_delivery.domain.order.controller;

import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAllReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAndDeliveryCreateReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderAllRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderIdRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderUpdateRes;
import com.rush.logistic.client.order_delivery.domain.order.service.OrderCreateService;
import com.rush.logistic.client.order_delivery.domain.order.service.OrderService;
import com.rush.logistic.client.order_delivery.global.auth.UserInfo;
import com.rush.logistic.client.order_delivery.global.auth.UserInfoHeader;
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
    private final OrderCreateService orderCreateService;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody OrderAndDeliveryCreateReq requestDto) {
        log.info("OrderController createOrder");
        Long tempUserId = 0L; // TODO : 사용자 정보 header 에서 받기

        OrderAllRes responseDto = orderCreateService.createDeliveryAndOrder(tempUserId, requestDto);
        return ResponseEntity.ok().body(BaseResponse.toResponse(OrderCode.CREATE_ORDER_OK, responseDto));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrderById(@UserInfoHeader UserInfo userInfo, @PathVariable UUID orderId) {
        log.info("OrderController getOrderById");

        log.info("getOrderById userInfo getUserId : {}", userInfo.getUserId());
        log.info("getOrderById userInfo getUsername : {}", userInfo.getUsername());
        log.info("getOrderById userInfo getRole : {}", userInfo.getRole());

        OrderAllRes responseDto = orderService.getOrderDetail(orderId);
        return ResponseEntity.ok().body(BaseResponse.toResponse(OrderCode.GET_ORDER_OK, responseDto));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<Object> updateOrder(@PathVariable UUID orderId, @RequestBody OrderAllReq requestDto) {
        log.info("OrderController updateOrder");

        OrderUpdateRes responseDto = orderService.updateOrder(orderId, requestDto);
        return ResponseEntity.ok().body(BaseResponse.toResponse(OrderCode.UPDATE_ORDER_OK, responseDto));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Object> deleteOrder(@PathVariable UUID orderId) {
        log.info("OrderController deleteOrder");

        Long userId = 10L; // TODO : 사용자 정보 header 에서 받기

        UUID deletedId = orderService.deleteOrder(orderId, userId);
        return ResponseEntity.ok().body(BaseResponse.toResponse(OrderCode.DELETE_ORDER_OK, OrderIdRes.toDto(deletedId)));
    }
}
