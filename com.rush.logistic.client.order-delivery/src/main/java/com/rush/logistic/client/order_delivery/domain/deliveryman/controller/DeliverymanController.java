package com.rush.logistic.client.order_delivery.domain.deliveryman.controller;

import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.request.DeliverymanCreateReq;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.request.DeliverymanUpdateReq;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response.DeliverymanAllRes;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response.DeliverymanIdRes;
import com.rush.logistic.client.order_delivery.domain.deliveryman.exception.DeliverymanCode;
import com.rush.logistic.client.order_delivery.domain.deliveryman.service.DeliverymanService;
import com.rush.logistic.client.order_delivery.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/deliverymans")
public class DeliverymanController {

    private final DeliverymanService deliverymanService;

    @PostMapping
    public ResponseEntity<Object> createDeliveryman(@RequestBody DeliverymanCreateReq requestDto) {
        log.info("DeliverymanController createDeliveryman");

        DeliverymanAllRes responseDto = deliverymanService.createDeliveryman(requestDto);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliverymanCode.CREATE_DELIVERYMAN_OK, responseDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getDeliverymanById(@PathVariable Long userId) {
        log.info("DeliverymanController getDeliverymanById");

        DeliverymanAllRes responseDto = deliverymanService.getDeliverymanDetail(userId);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliverymanCode.GET_DELIVERYMAN_OK, responseDto));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateDeliveryman(@PathVariable Long userId, @RequestBody DeliverymanUpdateReq requestDto) {
        log.info("DeliverymanController updateDeliveryman");

        DeliverymanAllRes responseDto = deliverymanService.updateDeliveryman(userId, requestDto);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliverymanCode.UPDATE_DELIVERYMAN_OK, responseDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteDeliveryman(@PathVariable Long userId) {
        log.info("DeliverymanController deleteDeliveryman");

        Long deleteUserId = 0L; // TODO : 임시 userId

        Long deletedId = deliverymanService.deleteDeliveryman(userId, deleteUserId);
        return ResponseEntity.ok().body(BaseResponse.toResponse(DeliverymanCode.DELETE_DELIVERYMAN_OK, DeliverymanIdRes.toDto(deletedId)));
    }
}
