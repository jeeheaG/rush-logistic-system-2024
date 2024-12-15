package com.rush.logistic.client.hub.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.HubIdResponseDto;
import com.rush.logistic.client.hub.dto.HubInfoRequestDto;
import com.rush.logistic.client.hub.dto.HubInfoResponseDto;
import com.rush.logistic.client.hub.dto.HubListResponseDto;
import com.rush.logistic.client.hub.dto.LatLonDto;
import com.rush.logistic.client.hub.message.HubMessage;
import com.rush.logistic.client.hub.model.Hub;
import com.rush.logistic.client.hub.model.HubItem;
import com.rush.logistic.client.hub.repository.HubItemRepository;
import com.rush.logistic.client.hub.repository.HubRepository;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public BaseResponseDto<HubIdResponseDto> createHub(HubInfoRequestDto requestDto) {
        HubIdResponseDto responseDto = null;
        // TODO: MASTER USER 확인 로직 추가
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
            else if (hubRepository.existsByName(requestDto.getName())) {
                Hub existHub = hubRepository.findByName(requestDto.getName());
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
            else if (hubRepository.existsByAddress(requestDto.getAddress())) {

                Hub existHub = hubRepository.findByAddress(requestDto.getName());
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
            Hub hub = hubRepository.save(Hub.from(requestDto, latLonDto));
            HubItem hubItem = hubItemRepository.save(HubItem.from(hub.getHubId(), requestDto, latLonDto));

            // 허브 생성 반환
            responseDto = HubIdResponseDto.from(hub.getHubId());

            return BaseResponseDto
                    .<HubIdResponseDto>from(HttpStatus.CREATED.value(), HttpStatus.CREATED, HubMessage.HUB_CREATED_SUCCESS.getMessage(), responseDto);
        } catch (Exception e) {
            return BaseResponseDto
                    .<HubIdResponseDto>from(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, HubMessage.HUB_SAVE_FAILED.getMessage(), null);
        }
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

            String originAddress = hub.getAddress();
            String updateAddress = requestDto.getAddress();

            if(!originAddress.equals(updateAddress)) {
                // 네이버 API : 주소 -> 좌표 추출
                String addressToCoordinatesResponse = getCoordinates(requestDto.getAddress());
                LatLonDto latLonDto = extractCoordinates(addressToCoordinatesResponse);

                // 허브 정보 업데이트
                hub.update(requestDto, latLonDto);
            }
            else {
                LatLonDto latLonDto = new LatLonDto(String.valueOf(hub.getLatitude()), String.valueOf(hub.getLongitude()));
                // 허브 정보 업데이트
                hub.update(requestDto, latLonDto);
            }

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
