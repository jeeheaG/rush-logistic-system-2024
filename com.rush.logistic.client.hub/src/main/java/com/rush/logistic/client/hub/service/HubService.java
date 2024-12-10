package com.rush.logistic.client.hub.service;

import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.HubIdResponseDto;
import com.rush.logistic.client.hub.dto.HubInfoRequestDto;
import com.rush.logistic.client.hub.dto.HubInfoResponseDto;
import com.rush.logistic.client.hub.dto.HubListResponseDto;
import com.rush.logistic.client.hub.message.HubMessage;
import com.rush.logistic.client.hub.model.Hub;
import com.rush.logistic.client.hub.repository.HubRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
        // TODO: 저장 실패시 예외 처리 추가

        // 허브 생성 반환
        HubIdResponseDto responseDto = HubIdResponseDto.from(hub.getHubId());

        return BaseResponseDto
                .<HubIdResponseDto>from(HttpStatus.CREATED.value(), HttpStatus.CREATED, HubMessage.HUB_CREATED_SUCCESS.getMessage(), responseDto);
    }

    public BaseResponseDto<HubInfoResponseDto> getHubDetails(UUID hubId) {
        try {
            // 허브 조회
            Hub hub = hubRepository.findById(hubId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(HubMessage.HUB_NOT_FOUND.getMessage())
                    );

            // soft delete된 허브입니다.
            if (hub.isDelete()) {
                return BaseResponseDto
                        .<HubInfoResponseDto>from(HttpStatus.GONE.value(), HttpStatus.GONE, HubMessage.HUB_ALREADY_DELETED.getMessage(), null);
            }

            // 허브 정보 반환
            HubInfoResponseDto responseDto = HubInfoResponseDto.from(hub);

            return BaseResponseDto
                    .<HubInfoResponseDto>from(HttpStatus.OK.value(), HttpStatus.OK, HubMessage.HUB_FOUND_SUCCESS.getMessage(), responseDto);
        }
        catch (IllegalArgumentException e) {
            return BaseResponseDto
                    .<HubInfoResponseDto>from(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, HubMessage.HUB_NOT_FOUND.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponseDto<HubInfoResponseDto> updateHubDetails(UUID hubId, HubInfoRequestDto requestDto) {
        // TODO: MASTER USER 확인 로직 추가
        try {
            // 허브 조회
            Hub hub = hubRepository.findById(hubId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(HubMessage.HUB_NOT_FOUND.getMessage())
                    );

            // soft delete된 허브입니다.
            if (hub.isDelete()) {
                return BaseResponseDto
                        .<HubInfoResponseDto>from(HttpStatus.GONE.value(), HttpStatus.GONE, HubMessage.HUB_ALREADY_DELETED.getMessage(), null);
            }

            // 허브 정보 업데이트
            hub.update(requestDto);

            // 업데이트 저장
            hubRepository.save(hub);

            return BaseResponseDto
                    .<HubInfoResponseDto>from(HttpStatus.OK.value(), HttpStatus.OK, HubMessage.HUB_UPDATED_SUCCESS.getMessage(), HubInfoResponseDto.from(hub));
        }catch (IllegalArgumentException e) {
            return BaseResponseDto
                    .<HubInfoResponseDto>from(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, HubMessage.HUB_NOT_FOUND.getMessage(), null);
        }
    }
  
    @Transactional
    public BaseResponseDto<HubIdResponseDto> deleteHub(UUID hubId) {
        // TODO: MASTER USER 확인 로직 추가
        try {
            // 허브 조회
            Hub hub = hubRepository.findById(hubId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(HubMessage.HUB_NOT_FOUND.getMessage())
                    );

            // soft delete된 허브입니다.
            if (hub.isDelete()) {
                return BaseResponseDto
                        .<HubIdResponseDto>from(HttpStatus.GONE.value(), HttpStatus.GONE, HubMessage.HUB_ALREADY_DELETED.getMessage(), null);
            }

            // 허브 삭제정보 업데이트
            hub.delete();

            // 허브 삭제정보 저장
            hubRepository.save(hub);

            return BaseResponseDto
                    .<HubIdResponseDto>from(HttpStatus.OK.value(), HttpStatus.OK, HubMessage.HUB_DELETED_SUCCESS.getMessage(), HubIdResponseDto.from(hub.getHubId()));
        }catch (IllegalArgumentException e) {
            return BaseResponseDto
                    .<HubIdResponseDto>from(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, HubMessage.HUB_NOT_FOUND.getMessage(), null);
        }
    }

    public BaseResponseDto<HubListResponseDto<HubInfoResponseDto>> getHubInfoList(
            int page, int size, String sortBy, boolean isAsc
    ) {
        try {
            Direction direction = isAsc ? Direction.ASC : Direction.DESC;
            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            // 허브 리스트 조회
            Page<Hub> hubList = hubRepository.findAllByIsDeleteFalse(pageable).orElseThrow(() ->
                    new NoSuchElementException(HubMessage.HUB_INFO_LIST_NOT_FOUND.getMessage())
            );

            List<HubInfoResponseDto> hubInfoList = hubList.stream()
                    .map(HubInfoResponseDto::from)
                    .toList();

            return BaseResponseDto
                    .<HubListResponseDto<HubInfoResponseDto>>from(HttpStatus.OK.value(), HttpStatus.OK, HubMessage.HUB_INFO_LIST_FOUND_SUCCESS.getMessage(), HubListResponseDto.from(hubInfoList));
        } catch (NoSuchElementException e) {
            return BaseResponseDto
                    .<HubListResponseDto<HubInfoResponseDto>>from(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, HubMessage.HUB_INFO_LIST_NOT_FOUND.getMessage(), null);

        }
    }
}
