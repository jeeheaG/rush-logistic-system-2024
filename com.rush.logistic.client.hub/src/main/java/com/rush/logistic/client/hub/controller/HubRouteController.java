package com.rush.logistic.client.hub.controller;

import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.HubPointRequestDto;
import com.rush.logistic.client.hub.dto.HubRouteIdResponseDto;
import com.rush.logistic.client.hub.dto.HubRouteInfoResponseDto;
import com.rush.logistic.client.hub.service.HubRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hubs-routes")
public class HubRouteController {

    private final HubRouteService hubRouteService;

    @PostMapping
    public ResponseEntity<BaseResponseDto<HubRouteIdResponseDto>> createHubRoute(@RequestBody HubPointRequestDto requestDto) {
        BaseResponseDto<HubRouteIdResponseDto> responseDto = hubRouteService.createHubRoute(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<BaseResponseDto<HubRouteInfoResponseDto>> getHubRouteInfo(@RequestBody HubPointRequestDto requestDto) {
        BaseResponseDto<HubRouteInfoResponseDto> responseDto = hubRouteService.getHubRouteInfo(requestDto);

        return ResponseEntity.ok(responseDto);
    }
}
