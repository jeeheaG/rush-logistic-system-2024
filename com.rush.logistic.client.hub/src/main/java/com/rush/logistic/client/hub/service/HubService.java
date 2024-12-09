package com.rush.logistic.client.hub.service;

import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.HubIdResponseDto;
import com.rush.logistic.client.hub.dto.HubInfoRequestDto;
import com.rush.logistic.client.hub.message.HubMessage;
import com.rush.logistic.client.hub.model.Hub;
import com.rush.logistic.client.hub.repository.HubRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubService {

    private final HubRepository hubRepository;

    @Transactional
    public BaseResponseDto<HubIdResponseDto> createHub(HubInfoRequestDto requestDto) {
        // TODO: MASTER USER 확인 로직 추가

        // 허브 저장
        Hub hub = hubRepository.save(Hub.from(requestDto));

        // 허브 생성 반환
        HubIdResponseDto responseDto = HubIdResponseDto.from(hub.getHubId());

        BaseResponseDto<HubIdResponseDto> response = BaseResponseDto
                .<HubIdResponseDto>from(HttpStatus.CREATED.value(), HttpStatus.CREATED, HubMessage.HUB_CREATED_SUCCESS.getMessage(), responseDto);

        return response;
    }
}
