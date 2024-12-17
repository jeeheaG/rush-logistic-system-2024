package com.rush.logistic.client.order_delivery.domain.delivery_route.controller;

import com.querydsl.core.types.Predicate;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.request.DeliveryRouteUpdateReq;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.response.DeliveryRouteAllRes;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.response.DeliveryRouteIdRes;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.delivery_route.exception.DeliveryRouteCode;
import com.rush.logistic.client.order_delivery.domain.delivery_route.service.DeliveryRouteService;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.global.auth.UserInfo;
import com.rush.logistic.client.order_delivery.global.auth.UserInfoHeader;
import com.rush.logistic.client.order_delivery.global.auth.UserRole;
import com.rush.logistic.client.order_delivery.global.auth.checker.DeliveryRouteUserRoleChecker;
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
@RequestMapping("/api/delivery-routes")
public class DeliveryRouteController {
    private final DeliveryRouteService deliveryRouteService;
    private final DeliveryRouteUserRoleChecker deliveryRouteUserRoleChecker;

//    /**
//     * 임시 api - api없이 주문or배달 쪽에서 내부호출 될 예정
//     * @param requestDto
//     * @return
//     */
//    @PostMapping
//    public ResponseEntity<Object> createDeliveryRoute(@RequestBody DeliveryRouteCreateReq requestDto) {
//        log.info("DeliveryRouteController createDeliveryRoute");
//
//        DeliveryRouteAllRes responseDto = deliveryRouteService.createDeliveryRoute(requestDto);
//        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryRouteCode.CREATE_DELIVERY_ROUTE_OK, responseDto));
//    }

    @GetMapping("/{deliveryRouteId}")
    public ResponseEntity<Object> getDeliveryRouteById(@UserInfoHeader UserInfo userInfo, @PathVariable UUID deliveryRouteId) {
        log.info("DeliveryRouteController getDeliveryRouteById");
        GetUserInfoRes getUserInfoRes = deliveryRouteUserRoleChecker.getUserAndCheckAllRole(userInfo);

        DeliveryRouteAllRes responseDto = deliveryRouteService.getDeliveryRouteDetail(deliveryRouteId, getUserInfoRes);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryRouteCode.GET_DELIVERY_ROUTE_OK, responseDto));
    }

    @GetMapping
    public ResponseEntity<Object> searchDeliveryRoute(@UserInfoHeader UserInfo userInfo,
                                                    @QuerydslPredicate(root = DeliveryRoute.class) Predicate predicate,
                                                    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("DeliveryRouteController searchDeliveryRoute");
        GetUserInfoRes getUserInfoRes = deliveryRouteUserRoleChecker.getUserAndCheckAllRole(userInfo);
        Pageable pageRequest = SearchUtil.checkAndGetPageRequest(pageable);

        PagedModel<DeliveryRouteAllRes> responseDtos = deliveryRouteService.getDeliveryRouteSearch(getUserInfoRes, predicate, pageRequest);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryRouteCode.SEARCH_DELIVERY_ROUTE_OK, responseDtos));
    }

    @PatchMapping("/{deliveryRouteId}")
    public ResponseEntity<Object> updateDeliveryRoute(@UserInfoHeader UserInfo userInfo, @PathVariable UUID deliveryRouteId, @RequestBody DeliveryRouteUpdateReq requestDto) {
        log.info("DeliveryRouteController updateDeliveryRoute");
        GetUserInfoRes getUserInfoRes = deliveryRouteUserRoleChecker.getUserAndCheckRole(Arrays.asList(UserRole.MASTER, UserRole.HUB, UserRole.DELIVERY), userInfo);

        DeliveryRouteAllRes responseDto = deliveryRouteService.updateDeliveryRoute(deliveryRouteId, requestDto, getUserInfoRes);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryRouteCode.UPDATE_DELIVERY_ROUTE_OK, responseDto));
    }

    @DeleteMapping("/{deliveryRouteId}")
    public ResponseEntity<Object> deleteDeliveryRoute(@UserInfoHeader UserInfo userInfo, @PathVariable UUID deliveryRouteId) {
        log.info("DeliveryRouteController deleteDeliveryRoute");
        GetUserInfoRes getUserInfoRes = deliveryRouteUserRoleChecker.getUserAndCheckRole(Arrays.asList(UserRole.MASTER, UserRole.HUB), userInfo);


        UUID deletedId = deliveryRouteService.deleteDeliveryRoute(deliveryRouteId, userInfo.getUserId(), getUserInfoRes);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryRouteCode.DELETE_DELIVERY_ROUTE_OK, DeliveryRouteIdRes.toDto(deletedId)));
    }
}
