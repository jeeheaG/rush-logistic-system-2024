package com.rush.logistic.client.hub.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.HubIdResponseDto;
import com.rush.logistic.client.hub.dto.HubInfoRequestDto;
import com.rush.logistic.client.hub.dto.HubInfoResponseDto;
import com.rush.logistic.client.hub.dto.HubListResponseDto;
import com.rush.logistic.client.hub.dto.LatLonDto;
import com.rush.logistic.client.hub.dto.UserDto;
import com.rush.logistic.client.hub.message.HubMessage;
import com.rush.logistic.client.hub.model.Hub;
import com.rush.logistic.client.hub.model.HubItem;
import com.rush.logistic.client.hub.repository.HubItemRepository;
import com.rush.logistic.client.hub.repository.HubRepository;
import com.rush.logistic.client.hub.repository.UserClient;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubService {

    @Value("${naver.map.client-id}")
    private String naverMapClientId;
    @Value("${naver.map.client-secret}")
    private String naverMapClientSecret;

    private final String GEOCODING_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
    private final String REVERSE_GEOCODING_URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc";
    private final String DIRECTION5_URL = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";
    private final String DIRECTION15_URL = "https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving";

    private final HubRepository hubRepository;
    private final HubItemRepository hubItemRepository;
    private final UserClient userClient;

    @Transactional
    public BaseResponseDto<HubIdResponseDto> createHub(Long userId, String role, HubInfoRequestDto requestDto) {
        HubIdResponseDto responseDto = null;
        boolean isNeedHubId = false;
        if (!checkForbidden(userId, role, isNeedHubId, null)) {
            return BaseResponseDto
                    .<HubIdResponseDto>from(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, HubMessage.HUB_CREATE_FORBIDDEN.getMessage(), null);
        }
        String username = getUserNameByJwt(userId, role);
        try {
            // 이미 추가된 허브인지 확인
            if(hubItemRepository.existsByName(requestDto.getName())) {
                //Redis에 있는지 먼저 확인
                HubItem existHubItem = hubItemRepository.findByName(requestDto.getName());
                responseDto = HubIdResponseDto.from(UUID.fromString(existHubItem.getHubId()));

                // 중복된 허브명
                return BaseResponseDto
                        .<HubIdResponseDto>from(HttpStatus.OK.value(), HttpStatus.OK, HubMessage.HUB_NAME_DUPLICATED.getMessage(), responseDto);
            }
            else if (hubRepository.existsByNameAndIsDeleteFalse(requestDto.getName())) {
                Hub existHub = hubRepository.findByNameAndIsDeleteFalse(requestDto.getName());
                responseDto = HubIdResponseDto.from(existHub.getHubId());

                // DB에 있는데 Redis에 캐싱 안되어있으면 redis에 추가
                HubItem registHubItem = hubItemRepository.save(HubItem.to(existHub));

                // 중복된 허브명
                return BaseResponseDto
                        .<HubIdResponseDto>from(HttpStatus.OK.value(), HttpStatus.OK, HubMessage.HUB_NAME_DUPLICATED.getMessage(), responseDto);
            }
            if(hubItemRepository.existsByAddress(requestDto.getAddress())) {
                //Redis에 있는지 먼저 확인
                HubItem existHubItem = hubItemRepository.findByAddress(requestDto.getAddress());
                responseDto = HubIdResponseDto.from(UUID.fromString(existHubItem.getHubId()));

                // 중복된 허브명
                return BaseResponseDto
                        .<HubIdResponseDto>from(HttpStatus.OK.value(), HttpStatus.OK, HubMessage.HUB_ADDRESS_DUPLICATED.getMessage(), responseDto);
            }
            else if (hubRepository.existsByAddressAndIsDeleteFalse(requestDto.getAddress())) {

                Hub existHub = hubRepository.findByAddressAndIsDeleteFalse(requestDto.getName());
                responseDto = HubIdResponseDto.from(existHub.getHubId());

                // DB에 있는데 Redis에 캐싱 안되어있으면 redis에 추가
                HubItem registHubItem = hubItemRepository.save(HubItem.to(existHub));

                // 중복된 주소
                return BaseResponseDto
                        .<HubIdResponseDto>from(HttpStatus.OK.value(), HttpStatus.OK, HubMessage.HUB_ADDRESS_DUPLICATED.getMessage(), responseDto);
            }

            // 신규 HUB 생성 로직
            // 네이버 API : 주소 -> 좌표 추출
            String addressToCoordinatesResponse = getCoordinates(requestDto.getAddress());
            LatLonDto latLonDto = extractCoordinates(addressToCoordinatesResponse);

            // 허브 저장
            Hub hub = hubRepository.save(Hub.from(requestDto, latLonDto, username));
            hubItemRepository.save(HubItem.from(hub.getHubId(), requestDto, latLonDto));

            // 허브 생성 반환
            responseDto = HubIdResponseDto.from(hub.getHubId());

            return BaseResponseDto
                    .<HubIdResponseDto>from(HttpStatus.CREATED.value(), HttpStatus.CREATED, HubMessage.HUB_CREATED_SUCCESS.getMessage(), responseDto);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return BaseResponseDto
                    .<HubIdResponseDto>from(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, HubMessage.HUB_SAVE_FAILED.getMessage(), null);
        }
    }

    private String getUserNameByJwt(Long userId, String role) {
        BaseResponseDto<UserDto> userDto = userClient.getUserById(userId, role, userId);
        return userDto.getData().getUsername();
    }

    private boolean checkForbidden(Long userId, String role, boolean isNeedHubId, UUID hubId) {
        BaseResponseDto<UserDto> userDto = userClient.getUserById(userId, role, userId);
        if(userDto.getData().getRole().equals("MASTER")) {
            return true;
        }
        if (userDto.getData().getRole().equals("HUB")) {
            if(isNeedHubId && userDto.getData().getHubId().equals(hubId)) {
                return true;
            }
            else if (!isNeedHubId) {
                return true;
            }
        }

        return false;
    }

    public BaseResponseDto<HubInfoResponseDto> getHubDetails(UUID hubId) {
        HubInfoResponseDto responseDto = null;
        try {
            if(hubItemRepository.existsById(String.valueOf(hubId))) {
                //Redis에 있는지 먼저 확인
                HubItem existHubItem = hubItemRepository.findById(String.valueOf(hubId)).get();
                responseDto = HubInfoResponseDto.fromRedis(existHubItem);

                // 중복된 허브명
                return BaseResponseDto
                        .<HubInfoResponseDto>from(HttpStatus.OK.value(), HttpStatus.OK, HubMessage.HUB_FOUND_SUCCESS.getMessage(), responseDto);
            }

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
            responseDto = HubInfoResponseDto.from(hub);

            // Redis에 저장
            HubItem hubItem = hubItemRepository.save(HubItem.to(hub));

            return BaseResponseDto
                    .<HubInfoResponseDto>from(HttpStatus.OK.value(), HttpStatus.OK, HubMessage.HUB_FOUND_SUCCESS.getMessage(), responseDto);
        }
        catch (IllegalArgumentException e) {
            return BaseResponseDto
                    .<HubInfoResponseDto>from(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, HubMessage.HUB_NOT_FOUND.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponseDto<HubInfoResponseDto> updateHubDetails(Long userId, String role, UUID hubId, HubInfoRequestDto requestDto) {
        boolean isNeedHubId = true;
        if (!checkForbidden(userId, role, isNeedHubId, hubId)) {
            return BaseResponseDto
                    .<HubInfoResponseDto>from(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, HubMessage.HUB_UPDATED_FORBIDDEN.getMessage(), null);
        }
        String username = getUserNameByJwt(userId, role);
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

            // 자기 자신을 제외한 이미 있는 hub이름, 주소 중복확인
            if(hubRepository.existsByNameAndIsDeleteFalse(requestDto.getName()) && !hub.getName().equals(requestDto.getName())) {
                return BaseResponseDto
                        .<HubInfoResponseDto>from(HttpStatus.OK.value(), HttpStatus.OK, HubMessage.HUB_NAME_DUPLICATED.getMessage(), HubInfoResponseDto.from(hub));
            }
            if(hubRepository.existsByAddressAndIsDeleteFalse(requestDto.getAddress()) && !hub.getAddress().equals(requestDto.getAddress())) {
                return BaseResponseDto
                        .<HubInfoResponseDto>from(HttpStatus.OK.value(), HttpStatus.OK, HubMessage.HUB_ADDRESS_DUPLICATED.getMessage(), HubInfoResponseDto.from(hub));
            }

            String originAddress = hub.getAddress();
            String updateAddress = requestDto.getAddress();

            if(!originAddress.equals(updateAddress)) {
                // 네이버 API : 주소 -> 좌표 추출
                String addressToCoordinatesResponse = getCoordinates(requestDto.getAddress());
                LatLonDto latLonDto = extractCoordinates(addressToCoordinatesResponse);

                // 허브 정보 업데이트
                hub.update(requestDto, latLonDto, username);
            }
            else {
                LatLonDto latLonDto = new LatLonDto(String.valueOf(hub.getLatitude()), String.valueOf(hub.getLongitude()));
                // 허브 정보 업데이트
                hub.update(requestDto, latLonDto, username);
            }

            // 업데이트 저장
            hubRepository.save(hub);

            // redis에 있다면 업데이트 내용 반영
            if(hubItemRepository.existsById(String.valueOf(hubId))) {
                HubItem hubItem = hubItemRepository.findById(String.valueOf(hubId)).get();
                hubItem.updateRedis(hub);
                hubItemRepository.save(hubItem);
            } else {
                // redis에 없다면 새로 추가
                hubItemRepository.save(HubItem.to(hub));
            }

            return BaseResponseDto
                    .<HubInfoResponseDto>from(HttpStatus.OK.value(), HttpStatus.OK, HubMessage.HUB_UPDATED_SUCCESS.getMessage(), HubInfoResponseDto.from(hub));
        }catch (IllegalArgumentException e) {
            return BaseResponseDto
                    .<HubInfoResponseDto>from(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, HubMessage.HUB_NOT_FOUND.getMessage(), null);
        }
    }
  
    @Transactional
    public BaseResponseDto<HubIdResponseDto> deleteHub(Long userId, String role, UUID hubId) {
        boolean isNeedHubId = true;
        if (!checkForbidden(userId, role, isNeedHubId, hubId)) {
            return BaseResponseDto
                    .<HubIdResponseDto>from(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, HubMessage.HUB_DELETED_FORBIDDEN.getMessage(), null);
        }
        String username = getUserNameByJwt(userId, role);
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
            hub.delete(username);

            // 허브 삭제정보 저장
            hubRepository.save(hub);

            // redis에 있다면 삭제
            if(hubItemRepository.existsById(String.valueOf(hubId))) {
                hubItemRepository.deleteById(String.valueOf(hubId));
            }

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
            Page<Hub> hubList = hubRepository.findPagedByIsDeleteFalse(pageable).orElseThrow(() ->
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

    public String getCoordinates(String address) {
        RestTemplate restTemplate = new RestTemplate();
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-ncp-apigw-api-key-id", naverMapClientId);
        headers.set("x-ncp-apigw-api-key", naverMapClientSecret);

        // 요청 URL 구성

        URI uri = UriComponentsBuilder.fromUriString(GEOCODING_URL)
                .queryParam("query", address)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                uri, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    public LatLonDto extractCoordinates(String jsonResponse) {
        try {
            LatLonDto latLon = new LatLonDto();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);

            // Geocoding 응답에서 첫 번째 주소의 위도와 경도 추출
            JsonNode addressNode = rootNode.path("addresses").get(0);
            String latitude = addressNode.path("y").asText();
            String longitude = addressNode.path("x").asText();

            latLon.setLatitude(latitude);
            latLon.setLongitude(longitude);

            return latLon;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("좌표 추출에 실패했습니다.");
        }
    }
}
