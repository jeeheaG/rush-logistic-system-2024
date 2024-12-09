package com.rush.logistic.client.hub.controller;

import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.HubIdResponseDto;
import com.rush.logistic.client.hub.dto.HubInfoRequestDto;
import com.rush.logistic.client.hub.dto.HubInfoResponseDto;
import com.rush.logistic.client.hub.service.HubService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/{hubId}")
    public ResponseEntity<BaseResponseDto<HubInfoResponseDto>> getHubDetails(@PathVariable("hubId") UUID hubId) {
        BaseResponseDto<HubInfoResponseDto> responseDto = hubService.getHubDetails(hubId);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{hubId}")
    public ResponseEntity<BaseResponseDto<HubInfoResponseDto>> updateHubDetails(@PathVariable("hubId") UUID hubId,
                                                                                @RequestBody HubInfoRequestDto requestDto) {
        BaseResponseDto<HubInfoResponseDto> responseDto = hubService.updateHubDetails(hubId, requestDto);

        return ResponseEntity.ok(responseDto);
    }
}
