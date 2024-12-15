package com.rush.logistic.client.order_delivery.domain.delivery_route.controller;

import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.request.DeliveryRouteCreateReq;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.request.DeliveryRouteUpdateReq;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.response.DeliveryRouteAllRes;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.response.DeliveryRouteIdRes;
import com.rush.logistic.client.order_delivery.domain.delivery_route.exception.DeliveryRouteCode;
import com.rush.logistic.client.order_delivery.domain.delivery_route.service.DeliveryRouteService;
import com.rush.logistic.client.order_delivery.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/delivery-routes")
public class DeliveryRouteController {
    private final DeliveryRouteService deliveryRouteService;

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
    public ResponseEntity<Object> getDeliveryRouteById(@PathVariable UUID deliveryRouteId) {
        log.info("DeliveryRouteController getDeliveryRouteById");

        DeliveryRouteAllRes responseDto = deliveryRouteService.getDeliveryRouteDetail(deliveryRouteId);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryRouteCode.GET_DELIVERY_ROUTE_OK, responseDto));
    }

    @PatchMapping("/{deliveryRouteId}")
    public ResponseEntity<Object> updateDeliveryRoute(@PathVariable UUID deliveryRouteId, @RequestBody DeliveryRouteUpdateReq requestDto) {
        log.info("DeliveryRouteController updateDeliveryRoute");

        DeliveryRouteAllRes responseDto = deliveryRouteService.updateDeliveryRoute(deliveryRouteId, requestDto);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryRouteCode.UPDATE_DELIVERY_ROUTE_OK, responseDto));
    }

    @DeleteMapping("/{deliveryRouteId}")
    public ResponseEntity<Object> deleteDeliveryRoute(@PathVariable UUID deliveryRouteId) {
        log.info("DeliveryRouteController deleteDeliveryRoute");

        Long deleteUserId = 0L; // TODO : 임시 deleteUserId

        UUID deletedId = deliveryRouteService.deleteDeliveryRoute(deliveryRouteId, deleteUserId);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryRouteCode.DELETE_DELIVERY_ROUTE_OK, DeliveryRouteIdRes.toDto(deletedId)));
    }
}
