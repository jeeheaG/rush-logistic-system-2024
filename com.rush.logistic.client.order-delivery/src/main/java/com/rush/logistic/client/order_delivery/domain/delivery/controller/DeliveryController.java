package com.rush.logistic.client.order_delivery.domain.delivery.controller;

import com.querydsl.core.types.Predicate;
import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.response.DeliveryIdRes;
import com.rush.logistic.client.order_delivery.domain.delivery.service.DeliveryService;
import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.request.DeliveryAllReq;
import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.response.DeliveryAllRes;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryCode;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.response.DeliveryRouteAllRes;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.delivery_route.exception.DeliveryRouteCode;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.global.auth.UserInfo;
import com.rush.logistic.client.order_delivery.global.auth.UserInfoHeader;
import com.rush.logistic.client.order_delivery.global.auth.UserRole;
import com.rush.logistic.client.order_delivery.global.auth.checker.DeliveryUserRoleChecker;
import com.rush.logistic.client.order_delivery.global.common.SearchUtil;
import com.rush.logistic.client.order_delivery.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final DeliveryUserRoleChecker deliveryUserRoleChecker;

//    // TODO : 테스트용 임시 api. 원래 delivery create 는  order 에서 내부 요청만 함
//    @PostMapping
//    public ResponseEntity<Object> createDelivery(@RequestBody DeliveryAllReq requestDto) {
//        log.info("DeliveryController createDelivery test api");
//
//        Delivery delivery = deliveryService.createDelivery(requestDto);
//        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryCode.CREATE_DELIVERY_OK, delivery));
//    }


    @GetMapping("/{deliveryId}")
    public ResponseEntity<Object> getDeliveryById(@UserInfoHeader UserInfo userInfo, @PathVariable UUID deliveryId) {
        log.info("DeliveryController getDeliveryById");
        GetUserInfoRes getUserInfoRes = deliveryUserRoleChecker.getUserAndCheckAllRole(userInfo);

        DeliveryAllRes responseDto = deliveryService.getDeliveryDetail(deliveryId, getUserInfoRes);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryCode.GET_DELIVERY_OK, responseDto));
    }

    @GetMapping
    public ResponseEntity<Object> searchDelivery(@UserInfoHeader UserInfo userInfo,
                                                      @QuerydslPredicate(root = DeliveryRoute.class) Predicate predicate,
                                                      @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("DeliveryController searchDelivery");
        GetUserInfoRes getUserInfoRes = deliveryUserRoleChecker.getUserAndCheckAllRole(userInfo);
        Pageable pageRequest = SearchUtil.checkAndGetPageRequest(pageable);

        PagedModel<DeliveryAllRes> responseDtos = deliveryService.getDeliverySearch(getUserInfoRes, predicate, pageRequest);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryCode.SEARCH_DELIVERY_OK, responseDtos));
    }

    @PatchMapping("/{deliveryId}")
    public ResponseEntity<Object> updateDelivery(@UserInfoHeader UserInfo userInfo, @PathVariable UUID deliveryId, @RequestBody DeliveryAllReq requestDto) {
        log.info("DeliveryController updateDelivery");
        GetUserInfoRes getUserInfoRes = deliveryUserRoleChecker.getUserAndCheckRole(Arrays.asList(UserRole.MASTER, UserRole.HUB, UserRole.DELIVERY), userInfo);

        DeliveryAllRes responseDto = deliveryService.updateDelivery(deliveryId, requestDto, getUserInfoRes);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryCode.UPDATE_DELIVERY_OK, responseDto));
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<Object> deleteDelivery(@UserInfoHeader UserInfo userInfo, @PathVariable UUID deliveryId) {
        log.info("DeliveryController deleteDelivery");
        GetUserInfoRes getUserInfoRes = deliveryUserRoleChecker.getUserAndCheckRole(Arrays.asList(UserRole.MASTER, UserRole.HUB), userInfo);

        UUID deletedId = deliveryService.deleteDelivery(deliveryId, userInfo.getUserId(), getUserInfoRes);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryCode.DELETE_DELIVERY_OK, DeliveryIdRes.toDto(deletedId)));
    }
}
