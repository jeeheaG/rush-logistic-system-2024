package com.rush.logistic.client.hub.controller;

import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.HubIdResponseDto;
import com.rush.logistic.client.hub.dto.HubInfoRequestDto;
import com.rush.logistic.client.hub.dto.HubInfoResponseDto;
import com.rush.logistic.client.hub.dto.HubListResponseDto;
import com.rush.logistic.client.hub.service.HubService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/api/hubs")
public class HubController {

    @Value("${server.port}")
    private String port;

    private final HubService hubService;

    @PostMapping
    public ResponseEntity<BaseResponseDto<HubIdResponseDto>> createHub(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @RequestBody HubInfoRequestDto requestDto) {
        BaseResponseDto<HubIdResponseDto> responseDto = hubService.createHub(userId, role, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{hubId}")
    public ResponseEntity<BaseResponseDto<HubInfoResponseDto>> getHubDetails(@PathVariable("hubId") UUID hubId) {
        BaseResponseDto<HubInfoResponseDto> responseDto = hubService.getHubDetails(hubId);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{hubId}")
    public ResponseEntity<BaseResponseDto<HubInfoResponseDto>> updateHubDetails(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @PathVariable("hubId") UUID hubId,
            @RequestBody HubInfoRequestDto requestDto) {
        BaseResponseDto<HubInfoResponseDto> responseDto = hubService.updateHubDetails(userId, role, hubId, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{hubId}")
    public ResponseEntity<BaseResponseDto<HubIdResponseDto>> deleteHub(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @PathVariable("hubId") UUID hubId) {
        BaseResponseDto<HubIdResponseDto> responseDto = hubService.deleteHub(userId, role, hubId);

        return ResponseEntity.ok(responseDto);
    }
  
    @GetMapping
    public ResponseEntity<BaseResponseDto<HubListResponseDto<HubInfoResponseDto>>> getHubInfoList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc,
            Sort sort
    ) {
        BaseResponseDto<HubListResponseDto<HubInfoResponseDto>> responseDto = hubService.getHubInfoList(
                page - 1, size, sortBy, isAsc
        );

        return ResponseEntity.ok(responseDto);
    }
}
