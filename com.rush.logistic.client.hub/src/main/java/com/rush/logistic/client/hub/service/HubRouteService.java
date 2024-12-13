package com.rush.logistic.client.hub.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.HubListResponseDto;
import com.rush.logistic.client.hub.dto.HubPointRequestDto;
import com.rush.logistic.client.hub.dto.HubRouteIdResponseDto;
import com.rush.logistic.client.hub.dto.HubRouteInfoResponseDto;
import com.rush.logistic.client.hub.dto.LatLonDto;
import com.rush.logistic.client.hub.dto.TimeTakenAndDistDto;
import com.rush.logistic.client.hub.message.HubMessage;
import com.rush.logistic.client.hub.message.HubRouteMessage;
import com.rush.logistic.client.hub.model.Hub;
import com.rush.logistic.client.hub.model.HubRoute;
import com.rush.logistic.client.hub.repository.HubRepository;
import com.rush.logistic.client.hub.repository.HubRouteRepository;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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
public class HubRouteService {

    @Value("${naver.map.client-id}")
    private String naverMapClientId;

    @Value("${naver.map.client-secret}")
    private String naverMapClientSecret;

    private final String GEOCODING_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
    private final String REVERSE_GEOCODING_URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc";
    private final String DIRECTION5_URL = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";
    private final String DIRECTION15_URL = "https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving";

    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;

    @Transactional
    public BaseResponseDto<HubRouteIdResponseDto> createHubRoute(HubPointRequestDto requestDto) {
        // TODO: 미리 구해둔 허브간 경로 정보가 있는지 확인 없다면 아래 로직 수행
        // TODO: 미리 구해둔 허브간 경로가 오래되었다면 아래 로직 수행해서 갱신
        // TODO: MASTER USER 확인 로직 추가
        try {
            // TODO: 이미 생성한 경로는 다시 생성할 필요 없음
            // TODO: 이미 생성한 경로가 soft delete되었다면 다시 생성해야함.
            // TODO: 일정 주기로 소요시간이 업데이트 될 순 있을거 같다.


            // 주소 추출
            String startAddress = extractAddress(requestDto.getStartHubId());
            String endAddress = extractAddress(requestDto.getEndHubId());

            // 네어지 지도 요청
//            String startAddressToCoordinatesResponse = getCoordinates(startAddress);
//            String endAddressToCoordinatesResponse = getCoordinates(endAddress);

//            LatLonDto startLatLon = extractCoordinates(startAddressToCoordinatesResponse);
//            LatLonDto endLatLon = extractCoordinates(endAddressToCoordinatesResponse);

            String startLat = String.valueOf(hubRepository.findById(requestDto.getStartHubId()).get().getLatitude());
            String startLon = String.valueOf(hubRepository.findById(requestDto.getStartHubId()).get().getLongitude());
            String endLat = String.valueOf(hubRepository.findById(requestDto.getEndHubId()).get().getLatitude());
            String endLon = String.valueOf(hubRepository.findById(requestDto.getEndHubId()).get().getLongitude());

            LatLonDto startLatLon = new LatLonDto(startLat, startLon);
            LatLonDto endLatLon = new LatLonDto(endLat, endLon);

            // 경로 탐색 -> 소요시간, 이동거리 추출
            String directionResponse = getTimeTackenAndDistance(startLatLon, endLatLon);
            TimeTakenAndDistDto timeTakenAndDistDto = extractTimeTakenAndDistance(directionResponse);

            // 허브 경로 저장
            Duration timeTaken = stringToDuration(timeTakenAndDistDto.getTimeTaken());
            int distance = Integer.parseInt(timeTakenAndDistDto.getDistance()); // 400Km
            System.out.println("소요시간 : " + timeTaken);
            HubRoute hubRoute = hubRouteRepository.save(HubRoute.from(requestDto, timeTaken, distance, timeTakenAndDistDto.getTimeTaken()));

            // 허브 경로 생성 반환
            HubRouteIdResponseDto responseDto = HubRouteIdResponseDto.from(hubRoute.getHubRouteId());

            return BaseResponseDto
                    .from(HttpStatus.CREATED.value(), HttpStatus.CREATED, HubRouteMessage.HUB_ROUTE_CREATED_SUCCESS.getMessage(), responseDto);
        } catch (IllegalArgumentException e) {
            return BaseResponseDto
                    .from(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, HubRouteMessage.HUB_ROUTE_NOT_CREATED.getMessage(), null);
        }
    }

    public BaseResponseDto<HubRouteInfoResponseDto> getHubRouteInfo(HubPointRequestDto requestDto) {
        try {
            HubRoute hubRoute = hubRouteRepository.findByStartHubIdAndEndHubId(requestDto.getStartHubId(),
                            requestDto.getEndHubId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(HubRouteMessage.HUB_ROUTE_NOT_FOUND.getMessage())
                    );

            // soft delete된 허브 경로 입니다.
            if (hubRoute.isDelete()){
                return BaseResponseDto
                        .from(HttpStatus.GONE.value(), HttpStatus.GONE, HubRouteMessage.HUB_ROUTE_ALREADY_DELETED.getMessage(), null);
            }

            String startHubName = hubRepository.findById(requestDto.getStartHubId()).get().getName();
            String startHubAddress = hubRepository.findById(requestDto.getStartHubId()).get().getAddress();
            String endHubName = hubRepository.findById(requestDto.getEndHubId()).get().getName();
            String endHubAddress = hubRepository.findById(requestDto.getEndHubId()).get().getAddress();

            HubRouteInfoResponseDto responseDto = HubRouteInfoResponseDto.from(
                    hubRoute, startHubName, startHubAddress, endHubName, endHubAddress);

            return BaseResponseDto
                    .from(HttpStatus.OK.value(), HttpStatus.OK, HubRouteMessage.HUB_ROUTE_FOUND.getMessage(),
                            responseDto);
        } catch (IllegalArgumentException e) {
            return BaseResponseDto
                    .from(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND,
                            HubRouteMessage.HUB_ROUTE_NOT_FOUND.getMessage(), null);
        }
    }

    public BaseResponseDto<HubRouteInfoResponseDto> getHubRouteInfoById(UUID hubRouteId) {
        try {
            HubRoute hubRoute = hubRouteRepository.findById(hubRouteId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(HubRouteMessage.HUB_ROUTE_NOT_FOUND.getMessage())
                    );

            // soft delete된 허브 경로 입니다.
            if (hubRoute.isDelete()){
                return BaseResponseDto
                        .from(HttpStatus.GONE.value(), HttpStatus.GONE, HubRouteMessage.HUB_ROUTE_ALREADY_DELETED.getMessage(), null);
            }

            UUID startHubId = hubRoute.getStartHubId();
            UUID endHubId = hubRoute.getEndHubId();

            String startHubName = hubRepository.findById(startHubId).get().getName();
            String startHubAddress = hubRepository.findById(startHubId).get().getAddress();
            String endHubName = hubRepository.findById(endHubId).get().getName();
            String endHubAddress = hubRepository.findById(endHubId).get().getAddress();

            HubRouteInfoResponseDto responseDto = HubRouteInfoResponseDto.from(
                    hubRoute, startHubName, startHubAddress, endHubName, endHubAddress);

            return BaseResponseDto
                    .from(HttpStatus.OK.value(), HttpStatus.OK, HubRouteMessage.HUB_ROUTE_FOUND.getMessage(),
                            responseDto);
        } catch (IllegalArgumentException e) {
            return BaseResponseDto
                    .from(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND,
                            HubRouteMessage.HUB_ROUTE_NOT_FOUND.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponseDto<HubRouteInfoResponseDto> updateHubRouteById(UUID hubRouteId) {
        try {
            HubRoute hubRoute = hubRouteRepository.findById(hubRouteId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(HubRouteMessage.HUB_ROUTE_NOT_FOUND.getMessage())
                    );

            // soft delete된 허브 경로 입니다.
            if (hubRoute.isDelete()){
                return BaseResponseDto
                        .from(HttpStatus.GONE.value(), HttpStatus.GONE, HubRouteMessage.HUB_ROUTE_ALREADY_DELETED.getMessage(), null);
            }

            String startHubName = hubRepository.findById(hubRoute.getStartHubId()).get().getName();
            String endHubName = hubRepository.findById(hubRoute.getEndHubId()).get().getName();

            String startHubAddress = extractAddress(hubRoute.getStartHubId());
            String endHubAddress = extractAddress(hubRoute.getEndHubId());

            String startCoordinates = getCoordinates(startHubAddress);
            String endCoordinates = getCoordinates(endHubAddress);

            LatLonDto startLatLon = extractCoordinates(startCoordinates);
            LatLonDto endLatLon = extractCoordinates(endCoordinates);

            String timeTakenAndDistance = getTimeTackenAndDistance(startLatLon, endLatLon);
            TimeTakenAndDistDto timeTakenAndDistDto = extractTimeTakenAndDistance(timeTakenAndDistance);

            // 허브 경로 정보 업데이트
            Duration timeTaken = stringToDuration(timeTakenAndDistDto.getTimeTaken());
            hubRoute.update(timeTakenAndDistDto, timeTaken);

            // 업데이트 저장
            hubRouteRepository.save(hubRoute);

            // 업데이트된 허브 경로 반환
            return BaseResponseDto
                    .from(HttpStatus.OK.value(), HttpStatus.OK, HubRouteMessage.HUB_ROUTE_UPDATED.getMessage(),
                            HubRouteInfoResponseDto.from(hubRoute, startHubName, startHubAddress, endHubName, endHubAddress));
        } catch (IllegalArgumentException e) {
            return BaseResponseDto
                    .from(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                            HubRouteMessage.HUB_ROUTE_NOT_UPDATED.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponseDto<HubRouteIdResponseDto> deleteHubRoute(UUID hubRouteId) {
        // TODO: MASTER USER 확인 로직 추가
        try {
            // 허브 조회
            HubRoute hubRoute = hubRouteRepository.findById(hubRouteId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(HubRouteMessage.HUB_ROUTE_NOT_FOUND.getMessage())
                    );

            // soft delete된 허브 경로 입니다.
            if (hubRoute.isDelete()){
                return BaseResponseDto
                        .from(HttpStatus.GONE.value(), HttpStatus.GONE, HubRouteMessage.HUB_ROUTE_ALREADY_DELETED.getMessage(), null);
            }

            // 허브 경로 삭제정보 업데이트
            hubRoute.delete();

            return BaseResponseDto
                    .from(HttpStatus.OK.value(), HttpStatus.OK, HubRouteMessage.HUB_ROUTE_DELETED_SUCCESS.getMessage(), HubRouteIdResponseDto.from(hubRoute.getHubRouteId()));
        } catch (Exception e) {
            return BaseResponseDto
                    .from(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, HubRouteMessage.HUB_ROUTE_NOT_FOUND.getMessage(), null);
        }
    }

    private Duration stringToDuration(String timeTaken) {
        long days = TimeUnit.MILLISECONDS.toDays(Long.parseLong(timeTaken));
        long hours = TimeUnit.MILLISECONDS.toHours(Long.parseLong(timeTaken)) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(timeTaken)) % 60;

        return Duration.ofDays(days).plusHours(hours).plusMinutes(minutes);
    }

    private TimeTakenAndDistDto extractTimeTakenAndDistance(String jsonResponse) {
        try {
            TimeTakenAndDistDto timeTakenAndDistDto = new TimeTakenAndDistDto();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);

            // 경로 탐색 결과에서 소요시간과 거리 추출
            JsonNode routeNode = rootNode.path("route").path("trafast").get(0).path("summary");
            String timeTaken = routeNode.path("duration").asText();
            String distance = routeNode.path("distance").asText();

            timeTakenAndDistDto.setTimeTaken(timeTaken);
            timeTakenAndDistDto.setDistance(distance);

            return timeTakenAndDistDto;
        } catch (Exception e){
            throw new IllegalArgumentException("시간과 거리 추출에 실패했습니다.");
        }
    }

    private String getTimeTackenAndDistance(LatLonDto startLatLon, LatLonDto endLatLon) {
        RestTemplate restTemplate = new RestTemplate();
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-ncp-apigw-api-key-id", naverMapClientId);
        headers.set("x-ncp-apigw-api-key", naverMapClientSecret);

        // 요청 URL 구성
        String satrtQuery = startLatLon.getLongitude() + "," + startLatLon.getLatitude();
        String endQuery = endLatLon.getLongitude() + "," + endLatLon.getLatitude();

        URI uri = UriComponentsBuilder.fromUriString(DIRECTION5_URL)
                .queryParam("start", satrtQuery)
                .queryParam("goal", endQuery)
                .queryParam("option", "trafast")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                uri, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    private String extractAddress(UUID startHubId) {
        Hub hub = hubRepository.findById(startHubId)
                .orElseThrow(() ->
                        new IllegalArgumentException(HubMessage.HUB_NOT_FOUND.getMessage())
                );

        return hub.getAddress();
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

    public BaseResponseDto<HubListResponseDto<HubRouteInfoResponseDto>> getHubRouteInfoList(
            int page, int size, String sortBy, boolean isAsc
    ) {
        try {
            Direction direction = isAsc ? Direction.ASC : Direction.DESC;
            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            // 허브 경로 리스트 조회
            Page<HubRoute> hubRouteList = hubRouteRepository.findAllByIsDeleteFalse(pageable).orElseThrow(() ->
                    new NoSuchElementException(HubRouteMessage.HUB_ROUTE_LIST_NOT_FOUND.getMessage())
            );

            List<HubRouteInfoResponseDto> hubRouteInfoList = new ArrayList<>();
            for(HubRoute hubRoute : hubRouteList){
                UUID startHubId = hubRoute.getStartHubId();
                UUID endHubId = hubRoute.getEndHubId();

                String startHubName = hubRepository.findById(startHubId).get().getName();
                String startHubAddress = hubRepository.findById(startHubId).get().getAddress();
                String endHubName = hubRepository.findById(endHubId).get().getName();
                String endHubAddress = hubRepository.findById(endHubId).get().getAddress();

                hubRouteInfoList.add(HubRouteInfoResponseDto.from(hubRoute, startHubName, startHubAddress, endHubName, endHubAddress));
            }

            return BaseResponseDto
                    .from(HttpStatus.OK.value(), HttpStatus.OK, HubRouteMessage.HUB_ROUTE_INFO_LIST_FOUND.getMessage(),
                            HubListResponseDto.from(hubRouteInfoList));
        } catch (NoSuchElementException e) {
            return BaseResponseDto
                    .from(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND,
                            HubRouteMessage.HUB_ROUTE_LIST_NOT_FOUND.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponseDto<HubListResponseDto<HubRouteIdResponseDto>> createHubRouteP2P(
            int page, int size, String sortBy, boolean isAsc
    ) {
        try {
            Direction direction = isAsc ? Direction.ASC : Direction.DESC;
            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Hub> hubList = hubRepository.findAllByIsDeleteFalse(pageable).orElseThrow(
                    () -> new NoSuchElementException(HubMessage.HUB_LIST_NOT_FOUND.getMessage())
            );

            for(Hub startHub : hubList){
                for(Hub endHub : hubList){
                    if(startHub.getHubId().equals(endHub.getHubId())){
                        continue;
                    }

                    System.out.print(startHub.getName() + " -> " + endHub.getName() + " 경로 생성");
                    HubPointRequestDto requestDto = new HubPointRequestDto(startHub.getHubId(), endHub.getHubId());
                    createHubRoute(requestDto);
                    System.out.println("---- 경로 생성 완료");
                }
            }

            return BaseResponseDto
                    .from(HttpStatus.CREATED.value(), HttpStatus.CREATED, HubRouteMessage.HUB_ROUTE_CREATED_SUCCESS.getMessage(), null);
        } catch (NoSuchElementException e){
            return BaseResponseDto
                    .from(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND,
                            HubMessage.HUB_LIST_NOT_FOUND.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponseDto<HubListResponseDto<HubRouteIdResponseDto>> createHubToHubRelay(
            int page, int size, String sortBy, boolean isAsc
    ) {
        try {
            Direction direction = isAsc ? Direction.ASC : Direction.DESC;
            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Hub> hubList = hubRepository.findAllByIsDeleteFalse(pageable).orElseThrow(
                    () -> new NoSuchElementException(HubMessage.HUB_LIST_NOT_FOUND.getMessage())
            );

            for(Hub startHub : hubList){
                for(Hub endHub : hubList){
                    if(startHub.getHubId().equals(endHub.getHubId())){
                        continue;
                    }

                    if(isLinkedRoute(startHub, endHub)){
                        // 연결된 허브라면 생성
                        Optional<HubRoute> connectedHubRoute = hubRouteRepository.findByStartHubIdAndEndHubId(startHub.getHubId(), endHub.getHubId());
                        if(!connectedHubRoute.isPresent()){
                            // 아직 생성되지 않았다면 신규 경로 생성
                            System.out.print(startHub.getName() + " -> " + endHub.getName() + " 경로 생성");
                            HubPointRequestDto requestDto = new HubPointRequestDto(startHub.getHubId(), endHub.getHubId());
                            createHubRoute(requestDto);
                            System.out.println("---- 경로 생성 완료");
                        } else {
                            if(connectedHubRoute.get().isDelete()){
                                // 생성했었지만 soft delete되어 있으면 다시 생성
                                connectedHubRoute.get().restore();
                                hubRouteRepository.save(connectedHubRoute.get());
                            }
                            // TODO: 생성한지 오래됐으면 최근 경로 반영
                        }

                    } else {
                        // 연결되지 않은 허브경로인데 존재 한다면 softDelete처리. isDelete -> true
                        Optional<HubRoute> unconnectedHubRoute = hubRouteRepository.findByStartHubIdAndEndHubIdAndIsDeleteFalse(startHub.getHubId(), endHub.getHubId());
                        if(unconnectedHubRoute.isPresent()){
                            unconnectedHubRoute.get().delete();
                            hubRouteRepository.save(unconnectedHubRoute.get());
                        }
                    }
                }
            }

            return BaseResponseDto
                    .from(HttpStatus.CREATED.value(), HttpStatus.CREATED, HubRouteMessage.HUB_ROUTE_CREATED_SUCCESS.getMessage(), null);
        } catch (NoSuchElementException e){
            return BaseResponseDto
                    .from(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND,
                            HubMessage.HUB_LIST_NOT_FOUND.getMessage(), null);
        }
    }

    private boolean isLinkedRoute(Hub startHub, Hub endHub) {
        // 서울
        if(startHub.getName().equals(HubName.SEOUL_HUB.getMessage()) && endHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage())){
            return true;
        }

        // 경기 북부
        if(startHub.getName().equals(HubName.NORTH_GYEONGGI_HUB.getMessage()) && endHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage())){
            return true;
        }

        // 경기 남부
        if(startHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage()) && endHub.getName().equals(HubName.SEOUL_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage()) && endHub.getName().equals(HubName.NORTH_GYEONGGI_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage()) && endHub.getName().equals(HubName.DAEGU_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage()) && endHub.getName().equals(HubName.INCHEON_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage()) && endHub.getName().equals(HubName.DAEJEON_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage()) && endHub.getName().equals(HubName.GANGWON_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage()) && endHub.getName().equals(HubName.GYEONGBUK_HUB.getMessage())){
            return true;
        }

        // 부산
        if(startHub.getName().equals(HubName.PUSAN_HUB.getMessage()) && endHub.getName().equals(HubName.DAEGU_HUB.getMessage())){
            return true;
        }

        // 대구
        if(startHub.getName().equals(HubName.DAEGU_HUB.getMessage()) && endHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.DAEGU_HUB.getMessage()) && endHub.getName().equals(HubName.PUSAN_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.DAEGU_HUB.getMessage()) && endHub.getName().equals(HubName.DAEJEON_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.DAEGU_HUB.getMessage()) && endHub.getName().equals(HubName.ULSAN_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.DAEGU_HUB.getMessage()) && endHub.getName().equals(HubName.GYEONGBUK_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.DAEGU_HUB.getMessage()) && endHub.getName().equals(HubName.GYEONGNAM_HUB.getMessage())){
            return true;
        }

        // 인천
        if(startHub.getName().equals(HubName.INCHEON_HUB.getMessage()) && endHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage())){
            return true;
        }

        // 광주
        if(startHub.getName().equals(HubName.GWANGJU_HUB.getMessage()) && endHub.getName().equals(HubName.DAEJEON_HUB.getMessage())){
            return true;
        }

        // 대전
        if(startHub.getName().equals(HubName.DAEJEON_HUB.getMessage()) && endHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.DAEJEON_HUB.getMessage()) && endHub.getName().equals(HubName.DAEGU_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.DAEJEON_HUB.getMessage()) && endHub.getName().equals(HubName.GWANGJU_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.DAEJEON_HUB.getMessage()) && endHub.getName().equals(HubName.SEJONG_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.DAEJEON_HUB.getMessage()) && endHub.getName().equals(HubName.CHUNGBUK_HUB.getMessage())) {
            return true;
        }
        if(startHub.getName().equals(HubName.DAEJEON_HUB.getMessage()) && endHub.getName().equals(HubName.CHUNGNAM_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.DAEJEON_HUB.getMessage()) && endHub.getName().equals(HubName.JEONBUK_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.DAEJEON_HUB.getMessage()) && endHub.getName().equals(HubName.JEONNAM_HUB.getMessage())){
            return true;
        }

        // 울산
        if(startHub.getName().equals(HubName.ULSAN_HUB.getMessage()) && endHub.getName().equals(HubName.DAEGU_HUB.getMessage())){
            return true;
        }

        // 세종
        if(startHub.getName().equals(HubName.SEJONG_HUB.getMessage()) && endHub.getName().equals(HubName.DAEJEON_HUB.getMessage())){
            return true;
        }

        // 강원
        if(startHub.getName().equals(HubName.GANGWON_HUB.getMessage()) && endHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage())){
            return true;
        }

        // 충북
        if(startHub.getName().equals(HubName.CHUNGBUK_HUB.getMessage()) && endHub.getName().equals(HubName.DAEJEON_HUB.getMessage())){
            return true;
        }

        // 충남
        if(startHub.getName().equals(HubName.CHUNGNAM_HUB.getMessage()) && endHub.getName().equals(HubName.DAEJEON_HUB.getMessage())){
            return true;
        }

        // 전북
        if(startHub.getName().equals(HubName.JEONBUK_HUB.getMessage()) && endHub.getName().equals(HubName.DAEJEON_HUB.getMessage())){
            return true;
        }

        // 전남
        if(startHub.getName().equals(HubName.JEONNAM_HUB.getMessage()) && endHub.getName().equals(HubName.DAEJEON_HUB.getMessage())){
            return true;
        }

        // 경북
        if(startHub.getName().equals(HubName.GYEONGBUK_HUB.getMessage()) && endHub.getName().equals(HubName.SOUTH_GYEONGGI_HUB.getMessage())){
            return true;
        }
        if(startHub.getName().equals(HubName.GYEONGBUK_HUB.getMessage()) && endHub.getName().equals(HubName.DAEGU_HUB.getMessage())){
            return true;
        }

        // 경남
        if(startHub.getName().equals(HubName.GYEONGNAM_HUB.getMessage()) && endHub.getName().equals(HubName.DAEGU_HUB.getMessage())){
            return true;
        }

        return false;
    }
}
