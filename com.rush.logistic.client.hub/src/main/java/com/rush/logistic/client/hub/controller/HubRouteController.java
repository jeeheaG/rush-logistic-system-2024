package com.rush.logistic.client.hub.controller;

import com.querydsl.core.types.Predicate;
import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.HubListResponseDto;
import com.rush.logistic.client.hub.dto.HubPointRequestDto;
import com.rush.logistic.client.hub.dto.HubRouteIdResponseDto;
import com.rush.logistic.client.hub.dto.HubRouteInfoResponseDto;
import com.rush.logistic.client.hub.dto.HubRouteListResponseDto;
import com.rush.logistic.client.hub.model.HubRoute;
import com.rush.logistic.client.hub.service.HubRouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/hubs-routes")
public class HubRouteController {

    private final HubRouteService hubRouteService;

    @Operation(summary = "HUB 간 경로 생성", description = "신규 HUB route를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공 or 이미 연결된 HUB route"),
            @ApiResponse(responseCode = "201", description = "신규 HUB route 생성 성공"),
            @ApiResponse(responseCode = "403", description = "해당 권한으로는 HUB route를 생성할 수 없습니다."),
            @ApiResponse(responseCode = "400", description = "예외 발생-신규 HUB route 생성 실패")
    })
    @PostMapping
    public ResponseEntity<BaseResponseDto<HubRouteIdResponseDto>> createHubRoute(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @RequestBody HubPointRequestDto requestDto) {
        BaseResponseDto<HubRouteIdResponseDto> responseDto = hubRouteService.createHubRoute(userId, role, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "HUB간 경로 조회", description = "두 HUB간 HUB route 연결정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "해당 HUB route를 찾을 수 없습니다.")
    })
    @GetMapping
    public ResponseEntity<BaseResponseDto<HubRouteListResponseDto<HubRouteInfoResponseDto>>> getHubRouteInfo(@RequestParam("startHubId") UUID startHubId,
                                                                                    @RequestParam("endHubId") UUID endHubId) {
        BaseResponseDto<HubRouteListResponseDto<HubRouteInfoResponseDto>> responseDto = hubRouteService.getHubRouteInfo(startHubId, endHubId);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "특정 HUB 경로 조회", description = "특정 HUB route를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "410", description = "이미 삭제한 HUB route"),
            @ApiResponse(responseCode = "404", description = "해당 HUB route를 찾을 수 없습니다.")
    })
    @GetMapping("/{hubRouteId}")
    public ResponseEntity<BaseResponseDto<HubRouteInfoResponseDto>> getHubRouteInfoById(@PathVariable("hubRouteId") UUID hubRouteId) {
        BaseResponseDto<HubRouteInfoResponseDto> responseDto = hubRouteService.getHubRouteInfoById(hubRouteId);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "HUB 간 경로 정보 업데이트", description = "특정 HUB route의 경로정보를 최신화합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "410", description = "이미 삭제한 HUB route"),
            @ApiResponse(responseCode = "403", description = "해당 HUB route를 수정할 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "해당 HUB route를 찾을 수 없습니다.")
    })
    @PutMapping("/{hubRouteId}")
    public ResponseEntity<BaseResponseDto<HubRouteInfoResponseDto>> updateHubRouteById(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @PathVariable("hubRouteId") UUID hubRouteId) {
        BaseResponseDto<HubRouteInfoResponseDto> responseDto = hubRouteService.updateHubRouteById(userId, role, hubRouteId);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "HUB간 경로 삭제", description = "특정 HUB route를 soft delete합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "410", description = "이미 삭제한 HUB"),
            @ApiResponse(responseCode = "403", description = "해당 HUB route를 삭제할 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "해당 HUB route를 찾을 수 없습니다.")
    })
    @DeleteMapping("/{hubRouteId}")
    public ResponseEntity<BaseResponseDto<HubRouteIdResponseDto>> deleteHubRoute(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @PathVariable("hubRouteId") UUID hubRouteId){
        BaseResponseDto<HubRouteIdResponseDto> responseDto = hubRouteService.deleteHubRoute(userId, role, hubRouteId);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "전체 HUB간 경로 목록 조회", description = "전체 HUB route 목록 조회(페이징) 및 검색(QueryDSL)합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "해당 HUB route를 찾을 수 없습니다.")
    })
    @GetMapping("/list")
    public ResponseEntity<BaseResponseDto<HubListResponseDto<HubRouteInfoResponseDto>>> getHubRouteInfoList(
            @RequestParam(required = false) List<UUID> idList,
            @QuerydslPredicate(root = HubRoute.class) Predicate predicate,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        BaseResponseDto<HubListResponseDto<HubRouteInfoResponseDto>> responseDto = hubRouteService.getHubRouteInfoList(
                idList, predicate, pageable
        );

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "HUB간 P2P 경로 조회", description = "전체 HUB의 P2P연결에 대한 경로(테스트용으로 경로도 자동 생성)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "해당 HUB route를 찾을 수 없습니다.")
    })
    @PostMapping("/createP2P")
    public ResponseEntity<BaseResponseDto<HubRouteListResponseDto<HubRouteInfoResponseDto>>> createHubRouteP2P(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @RequestBody HubPointRequestDto requestDto) {
        BaseResponseDto<HubRouteListResponseDto<HubRouteInfoResponseDto>> responseDto = hubRouteService.createHubRouteP2P(userId, role, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "HUB간 HUB to HUB relay 경로 조회", description = "전체 HUB의 HUB to HUB relay연결에 대한 경로(테스트용으로 경로도 자동 생성)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "해당 HUB route를 찾을 수 없습니다.")
    })
    @PostMapping("/createHubToHubRelay")
    public ResponseEntity<BaseResponseDto<HubRouteListResponseDto<HubRouteInfoResponseDto>>> createHubToHubRelay(
            @RequestHeader(value = "USER_ID", required = true) Long userId,
            @RequestHeader(value = "role", required = true) String role,
            @RequestBody HubPointRequestDto requestDto) {
        BaseResponseDto<HubRouteListResponseDto<HubRouteInfoResponseDto>> responseDto = hubRouteService.createHubToHubRelay(userId, role, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
