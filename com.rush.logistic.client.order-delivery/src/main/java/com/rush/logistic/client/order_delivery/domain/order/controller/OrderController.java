package com.rush.logistic.client.order_delivery.domain.order.controller;

import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAllReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAndDeliveryCreateReq;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderAllRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderIdRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderUpdateRes;
import com.rush.logistic.client.order_delivery.domain.order.service.OrderCreateService;
import com.rush.logistic.client.order_delivery.domain.order.service.OrderService;
import com.rush.logistic.client.order_delivery.global.auth.UserInfo;
import com.rush.logistic.client.order_delivery.global.auth.UserInfoHeader;
import com.rush.logistic.client.order_delivery.global.auth.UserRole;
import com.rush.logistic.client.order_delivery.global.auth.checker.OrderUserRoleChecker;
import com.rush.logistic.client.order_delivery.global.response.BaseResponse;
import com.rush.logistic.client.order_delivery.domain.order.exception.OrderCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderCreateService orderCreateService;
    private final OrderService orderService;
    private final OrderUserRoleChecker orderUserRoleChecker;

    @PostMapping
    public ResponseEntity<Object> createOrder(@UserInfoHeader UserInfo userInfo, @RequestBody OrderAndDeliveryCreateReq requestDto) {
        log.info("OrderController createOrder");
        GetUserInfoRes getUserInfoRes = orderUserRoleChecker.getUserAndCheckAllRole(userInfo);

        OrderAllRes responseDto = orderCreateService.createDeliveryAndOrder(userInfo.getUserId(), requestDto);
        return ResponseEntity.ok().body(BaseResponse.toResponse(OrderCode.CREATE_ORDER_OK, responseDto));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrderById(@UserInfoHeader UserInfo userInfo, @PathVariable UUID orderId) {
        log.info("OrderController getOrderById");
        GetUserInfoRes getUserInfoRes = orderUserRoleChecker.getUserAndCheckAllRole(userInfo);

        OrderAllRes responseDto = orderService.getOrderDetail(orderId, getUserInfoRes);
        return ResponseEntity.ok().body(BaseResponse.toResponse(OrderCode.GET_ORDER_OK, responseDto));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<Object> updateOrder(@UserInfoHeader UserInfo userInfo, @PathVariable UUID orderId, @RequestBody OrderAllReq requestDto) {
        log.info("OrderController updateOrder");
        GetUserInfoRes getUserInfoRes = orderUserRoleChecker.getUserAndCheckRole(Arrays.asList(UserRole.MASTER, UserRole.HUB), userInfo);

        OrderUpdateRes responseDto = orderService.updateOrder(orderId, requestDto, getUserInfoRes);
        return ResponseEntity.ok().body(BaseResponse.toResponse(OrderCode.UPDATE_ORDER_OK, responseDto));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Object> deleteOrder(@UserInfoHeader UserInfo userInfo, @PathVariable UUID orderId) {
        log.info("OrderController deleteOrder");
        GetUserInfoRes getUserInfoRes = orderUserRoleChecker.getUserAndCheckRole(Arrays.asList(UserRole.MASTER, UserRole.HUB), userInfo);

        UUID deletedId = orderService.deleteOrder(orderId, userInfo.getUserId(), getUserInfoRes);
        return ResponseEntity.ok().body(BaseResponse.toResponse(OrderCode.DELETE_ORDER_OK, OrderIdRes.toDto(deletedId)));
    }

}
