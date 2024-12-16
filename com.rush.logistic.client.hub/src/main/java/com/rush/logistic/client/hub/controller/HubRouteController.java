package com.rush.logistic.client.hub.controller;

import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.HubListResponseDto;
import com.rush.logistic.client.hub.dto.HubPointRequestDto;
import com.rush.logistic.client.hub.dto.HubRouteIdResponseDto;
import com.rush.logistic.client.hub.dto.HubRouteInfoResponseDto;
import com.rush.logistic.client.hub.dto.HubRouteListResponseDto;
import com.rush.logistic.client.hub.service.HubRouteService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hubs-routes")
public class HubRouteController {

    private final HubRouteService hubRouteService;

    @PostMapping
    public ResponseEntity<BaseResponseDto<HubRouteIdResponseDto>> createHubRoute(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @RequestBody HubPointRequestDto requestDto) {
        BaseResponseDto<HubRouteIdResponseDto> responseDto = hubRouteService.createHubRoute(userId, role, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<BaseResponseDto<HubRouteListResponseDto<HubRouteInfoResponseDto>>> getHubRouteInfo(@RequestParam("startHubId") UUID startHubId,
                                                                                    @RequestParam("endHubId") UUID endHubId) {
        BaseResponseDto<HubRouteListResponseDto<HubRouteInfoResponseDto>> responseDto = hubRouteService.getHubRouteInfo(startHubId, endHubId);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{hubRouteId}")
    public ResponseEntity<BaseResponseDto<HubRouteInfoResponseDto>> getHubRouteInfoById(@PathVariable("hubRouteId") UUID hubRouteId) {
        BaseResponseDto<HubRouteInfoResponseDto> responseDto = hubRouteService.getHubRouteInfoById(hubRouteId);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{hubRouteId}")
    public ResponseEntity<BaseResponseDto<HubRouteInfoResponseDto>> updateHubRouteById(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @PathVariable("hubRouteId") UUID hubRouteId) {
        BaseResponseDto<HubRouteInfoResponseDto> responseDto = hubRouteService.updateHubRouteById(userId, role, hubRouteId);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{hubRouteId}")
    public ResponseEntity<BaseResponseDto<HubRouteIdResponseDto>> deleteHubRoute(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @PathVariable("hubRouteId") UUID hubRouteId){
        BaseResponseDto<HubRouteIdResponseDto> responseDto = hubRouteService.deleteHubRoute(userId, role, hubRouteId);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/list")
    public ResponseEntity<BaseResponseDto<HubListResponseDto<HubRouteInfoResponseDto>>> getHubRouteInfoList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc,
            Sort sort
    ) {
        BaseResponseDto<HubListResponseDto<HubRouteInfoResponseDto>> responseDto = hubRouteService.getHubRouteInfoList(
                page - 1, size, sortBy, isAsc
        );

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/createP2P")
    public ResponseEntity<BaseResponseDto<HubRouteListResponseDto<HubRouteInfoResponseDto>>> createHubRouteP2P(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @RequestBody HubPointRequestDto requestDto) {
        BaseResponseDto<HubRouteListResponseDto<HubRouteInfoResponseDto>> responseDto = hubRouteService.createHubRouteP2P(userId, role, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/createHubToHubRelay")
    public ResponseEntity<BaseResponseDto<HubRouteListResponseDto<HubRouteInfoResponseDto>>> createHubToHubRelay(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @RequestBody HubPointRequestDto requestDto) {
        BaseResponseDto<HubRouteListResponseDto<HubRouteInfoResponseDto>> responseDto = hubRouteService.createHubToHubRelay(userId, role, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
