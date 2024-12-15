package com.rush.logistic.client.order_delivery.domain.deliveryman.controller;

import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.request.DeliverymanCreateReq;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.request.DeliverymanUpdateReq;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response.DeliverymanAllRes;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response.DeliverymanIdRes;
import com.rush.logistic.client.order_delivery.domain.deliveryman.exception.DeliverymanCode;
import com.rush.logistic.client.order_delivery.domain.deliveryman.service.DeliverymanService;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.global.auth.UserInfo;
import com.rush.logistic.client.order_delivery.global.auth.UserInfoHeader;
import com.rush.logistic.client.order_delivery.global.auth.UserRole;
import com.rush.logistic.client.order_delivery.global.auth.checker.DeliverymanUserRoleChecker;
import com.rush.logistic.client.order_delivery.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/deliverymans")
public class DeliverymanController {

    private final DeliverymanService deliverymanService;
    private final DeliverymanUserRoleChecker deliverymanUserRoleChecker;

    @PostMapping
    public ResponseEntity<Object> createDeliveryman(@UserInfoHeader UserInfo userInfo, @RequestBody DeliverymanCreateReq requestDto) {
        log.info("DeliverymanController createDeliveryman");
        GetUserInfoRes getUserInfoRes = deliverymanUserRoleChecker.getUserAndCheckRole(getMasterAndHubRole(), userInfo);

        DeliverymanAllRes responseDto = deliverymanService.createDeliveryman(requestDto, getUserInfoRes);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliverymanCode.CREATE_DELIVERYMAN_OK, responseDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getDeliverymanById(@UserInfoHeader UserInfo userInfo, @PathVariable Long userId) {
        log.info("DeliverymanController getDeliverymanById");
        GetUserInfoRes getUserInfoRes = deliverymanUserRoleChecker.getUserAndCheckRole(Arrays.asList(UserRole.MASTER, UserRole.HUB, UserRole.DELIVERY), userInfo);

        DeliverymanAllRes responseDto = deliverymanService.getDeliverymanDetail(userId, getUserInfoRes);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliverymanCode.GET_DELIVERYMAN_OK, responseDto));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateDeliveryman(@UserInfoHeader UserInfo userInfo, @PathVariable Long userId, @RequestBody DeliverymanUpdateReq requestDto) {
        log.info("DeliverymanController updateDeliveryman");
        GetUserInfoRes getUserInfoRes = deliverymanUserRoleChecker.getUserAndCheckRole(getMasterAndHubRole(), userInfo);

        DeliverymanAllRes responseDto = deliverymanService.updateDeliveryman(userId, requestDto, getUserInfoRes);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliverymanCode.UPDATE_DELIVERYMAN_OK, responseDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteDeliveryman(@UserInfoHeader UserInfo userInfo, @PathVariable Long userId) {
        log.info("DeliverymanController deleteDeliveryman");
        GetUserInfoRes getUserInfoRes = deliverymanUserRoleChecker.getUserAndCheckRole(getMasterAndHubRole(), userInfo);

        Long deletedId = deliverymanService.deleteDeliveryman(userId, userInfo.getUserId(), getUserInfoRes);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliverymanCode.DELETE_DELIVERYMAN_OK, DeliverymanIdRes.toDto(deletedId)));
    }

    private static List<UserRole> getMasterAndHubRole() {
        return Arrays.asList(UserRole.MASTER, UserRole.HUB);
    }
}
