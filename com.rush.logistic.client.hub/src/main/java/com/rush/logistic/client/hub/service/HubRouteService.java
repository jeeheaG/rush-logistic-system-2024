package com.rush.logistic.client.hub.service;

import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.HubPointRequestDto;
import com.rush.logistic.client.hub.dto.HubRouteIdResponseDto;
import com.rush.logistic.client.hub.message.HubRouteMessage;
import com.rush.logistic.client.hub.model.HubRoute;
import com.rush.logistic.client.hub.repository.HubRouteRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubRouteService {

    private final HubRouteRepository hubRouteRepository;

    @Transactional
    public BaseResponseDto<HubRouteIdResponseDto> createHubRoute(HubPointRequestDto requestDto) {
        // TODO: MASTER USER 확인 로직 추가
        try {
            // TODO: 이미 생성한 경로는 다시 생성할 필요 없음
            // TODO: 일정 주기로 소요시간이 업데이트 될 순 있을거 같다.

            // 허브 경로 저장
            Duration timeTaken = Duration.ofDays(1).plusHours(6).plusMinutes(30); // 예시로 1일 6시간 30분
//            Duration timeTaken = Duration.ofHours(6).plusMinutes(30); // 예시로 6시간 30분
            int distance = 400000; // 400Km
            System.out.println("소요시간 : " + timeTaken);
            HubRoute hubRoute = hubRouteRepository.save(HubRoute.from(requestDto, timeTaken, distance));

            // 허브 경로 생성 반환
            HubRouteIdResponseDto responseDto = HubRouteIdResponseDto.from(hubRoute.getHubRouteId());

            return BaseResponseDto
                    .from(HttpStatus.CREATED.value(), HttpStatus.CREATED, HubRouteMessage.HUB_ROUTE_CREATED_SUCCESS.getMessage(), responseDto);
        } catch (IllegalArgumentException e) {
            return BaseResponseDto
                    .from(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, HubRouteMessage.HUB_ROUTE_NOT_CREATED.getMessage(), null);
        }
    }
}
