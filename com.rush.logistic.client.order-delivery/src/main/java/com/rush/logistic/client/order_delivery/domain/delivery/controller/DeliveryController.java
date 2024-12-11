package com.rush.logistic.client.order_delivery.domain.delivery.controller;

import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.response.DeliveryIdRes;
import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery.service.DeliveryService;
import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.request.DeliveryAllReq;
import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.response.DeliveryAllRes;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryCode;
import com.rush.logistic.client.order_delivery.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;

//    // TODO : 테스트용 임시 api. 원래 delivery create 는  order 에서 내부 요청만 함
//    @PostMapping
//    public ResponseEntity<Object> createDelivery(@RequestBody DeliveryAllReq requestDto) {
//        log.info("DeliveryController createDelivery test api");
//
//        Delivery delivery = deliveryService.createDelivery(requestDto);
//        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryCode.CREATE_DELIVERY_OK, delivery));
//    }


    @GetMapping("/{deliveryId}")
    public ResponseEntity<Object> getDeliveryById(@PathVariable UUID deliveryId) {
        log.info("DeliveryController getDeliveryById");

        DeliveryAllRes responseDto = deliveryService.getDeliveryDetail(deliveryId);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryCode.GET_DELIVERY_OK, responseDto));
    }

    @PatchMapping("/{deliveryId}")
    public ResponseEntity<Object> updateDelivery(@PathVariable UUID deliveryId, @RequestBody DeliveryAllReq requestDto) {
        log.info("DeliveryController updateDelivery");

        DeliveryAllRes responseDto = deliveryService.updateDelivery(deliveryId, requestDto);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryCode.UPDATE_DELIVERY_OK, responseDto));
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<Object> deleteDelivery(@PathVariable UUID deliveryId) {
        log.info("DeliveryController deleteDelivery");

        UUID userId = UUID.randomUUID(); // TODO : 임시 UUID

        UUID deletedId = deliveryService.deleteDelivery(deliveryId, userId);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliveryCode.DELETE_DELIVERY_OK, DeliveryIdRes.toDto(deletedId)));
    }
}
