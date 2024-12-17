package com.rush.logistic.client.hub.controller;

import com.querydsl.core.types.Predicate;
import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.HubIdResponseDto;
import com.rush.logistic.client.hub.dto.HubInfoRequestDto;
import com.rush.logistic.client.hub.dto.HubInfoResponseDto;
import com.rush.logistic.client.hub.dto.HubListResponseDto;
import com.rush.logistic.client.hub.model.Hub;
import com.rush.logistic.client.hub.service.HubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
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

    @Operation(summary = "신규 HUB 생성", description = "신규 HUB를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공 or 이미 추가했던 HUB"),
            @ApiResponse(responseCode = "201", description = "신규 HUB 생성 성공"),
            @ApiResponse(responseCode = "403", description = "해당 권한으로는 HUB를 생성할 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "예외 발생-신규 HUB 생성 실패")
    })
    @PostMapping
    public ResponseEntity<BaseResponseDto<HubIdResponseDto>> createHub(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @RequestBody HubInfoRequestDto requestDto) {
        BaseResponseDto<HubIdResponseDto> responseDto = hubService.createHub(userId, role, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "특정 HUB 조회", description = "특정 HUB를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "410", description = "이미 삭제한 HUB"),
            @ApiResponse(responseCode = "404", description = "해당 HUB를 찾을 수 없습니다.")
    })
    @GetMapping("/{hubId}")
    public ResponseEntity<BaseResponseDto<HubInfoResponseDto>> getHubDetails(@PathVariable("hubId") UUID hubId) {
        BaseResponseDto<HubInfoResponseDto> responseDto = hubService.getHubDetails(hubId);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "특정 HUB 수정", description = "특정 HUB를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "410", description = "이미 삭제한 HUB"),
            @ApiResponse(responseCode = "403", description = "해당 HUB를 수정할 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "해당 HUB를 찾을 수 없습니다.")
    })
    @PutMapping("/{hubId}")
    public ResponseEntity<BaseResponseDto<HubInfoResponseDto>> updateHubDetails(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @PathVariable("hubId") UUID hubId,
            @RequestBody HubInfoRequestDto requestDto) {
        BaseResponseDto<HubInfoResponseDto> responseDto = hubService.updateHubDetails(userId, role, hubId, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "특정 HUB 삭제", description = "특정 HUB를 soft delete합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "410", description = "이미 삭제한 HUB"),
            @ApiResponse(responseCode = "403", description = "해당 HUB를 삭제할 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "해당 HUB를 찾을 수 없습니다.")
    })
    @DeleteMapping("/{hubId}")
    public ResponseEntity<BaseResponseDto<HubIdResponseDto>> deleteHub(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @PathVariable("hubId") UUID hubId) {
        BaseResponseDto<HubIdResponseDto> responseDto = hubService.deleteHub(userId, role, hubId);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "전체 HUB 목록 조회", description = "전체 HUB 목록 조회(페이징) 및 검색(QueryDSL)합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "해당 HUB를 찾을 수 없습니다.")
    })
    @GetMapping
    public ResponseEntity<BaseResponseDto<HubListResponseDto<HubInfoResponseDto>>> getHubInfoList(
            @RequestParam(required = false) List<UUID> idList,
            @QuerydslPredicate(root = Hub.class) Predicate predicate,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        BaseResponseDto<HubListResponseDto<HubInfoResponseDto>> responseDto = hubService.getHubInfoList(
                idList, predicate, pageable
        );

        return ResponseEntity.ok(responseDto);
    }
}
