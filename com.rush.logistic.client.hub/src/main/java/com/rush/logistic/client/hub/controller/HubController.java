package com.rush.logistic.client.hub.controller;

import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.HubIdResponseDto;
import com.rush.logistic.client.hub.dto.HubInfoRequestDto;
import com.rush.logistic.client.hub.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hubs")
public class HubController {

    @Value("${server.port}")
    private String port;

    private final HubService hubService;

    @PostMapping
    public ResponseEntity<BaseResponseDto<HubIdResponseDto>> createHub(@RequestBody HubInfoRequestDto requestDto) {
        BaseResponseDto<HubIdResponseDto> responseDto = hubService.createHub(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
