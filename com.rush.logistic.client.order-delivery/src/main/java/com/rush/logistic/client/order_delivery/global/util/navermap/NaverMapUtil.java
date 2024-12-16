package com.rush.logistic.client.order_delivery.global.util.navermap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class NaverMapUtil {

    private static String clientId;
    private static String clientSecret;

    public NaverMapUtil(@Value("${NAVER_MAP_CLIENT_ID}") String clientId,
                        @Value("${NAVER_MAP_CLIENT_SECRET}") String clientSecret) {
        NaverMapUtil.clientId = clientId;
        NaverMapUtil.clientSecret = clientSecret;
    }

    private static String GEOCODING_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
    private static String DIRECTION5_URL = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";

    public NaverMapRes getDistanceAndTimeByAddress(String startAddress, String endAddress) {
        // api call 로직 구현
        System.out.println("네이버 클라이언트ID " + clientId);
        System.out.println("네이버 시크릿ID " + clientSecret);

        // 주소 -> 좌표
        String startAddressToCoordinatesResponse = getCoordinates(startAddress);
        LatLonDto startLatLon = extractCoordinates(startAddressToCoordinatesResponse);
        String endAddressToCoordinatesResponse = getCoordinates(endAddress);
        LatLonDto endLatLon = extractCoordinates(endAddressToCoordinatesResponse);

        // 경로 탐색
        String directionResponse = getTimeTackenAndDistance(startLatLon, endLatLon);
        TimeTakenAndDistDto timeTakenAndDistDto = extractTimeTakenAndDistance(directionResponse);

        Duration duration = stringToDuration(timeTakenAndDistDto.getTimeTaken());
        String timeTaken = formatDuration(duration);

        return NaverMapRes.toDto(timeTakenAndDistDto.getDistance(), timeTaken.toString());
    }

    private static String getCoordinates(String address) {
        RestTemplate restTemplate = new RestTemplate();
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-ncp-apigw-api-key-id", clientId);
        headers.set("x-ncp-apigw-api-key", clientSecret);

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

    private static LatLonDto extractCoordinates(String jsonResponse) {
        try {
            LatLonDto latLon = new LatLonDto();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            log.info("rootNode : {}", rootNode);

            // Geocoding 응답에서 첫 번째 주소의 위도와 경도 추출
            JsonNode addressNode = rootNode.path("addresses").get(0);
            log.info("addressNode : {}", addressNode);
            String latitude = addressNode.path("y").asText();
            log.info("addressNode latitude : {}", latitude);
            String longitude = addressNode.path("x").asText();
            log.info("addressNode longitude : {}", longitude);

            latLon.setLatitude(latitude);
            latLon.setLongitude(longitude);

            return latLon;
        } catch (Exception e) {
            throw new RuntimeException("주소 -> 좌표 변환 실패");
        }
    }

    private static String getTimeTackenAndDistance(LatLonDto startLatLon, LatLonDto endLatLon) {
        RestTemplate restTemplate = new RestTemplate();
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-ncp-apigw-api-key-id", clientId);
        headers.set("x-ncp-apigw-api-key", clientSecret);

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

    private static TimeTakenAndDistDto extractTimeTakenAndDistance(String jsonResponse) {
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

    private static Duration stringToDuration(String timeTaken) {
        long days = TimeUnit.MILLISECONDS.toDays(Long.parseLong(timeTaken));
        long hours = TimeUnit.MILLISECONDS.toHours(Long.parseLong(timeTaken)) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(timeTaken)) % 60;

        return Duration.ofDays(days).plusHours(hours).plusMinutes(minutes);
    }

    private static String formatDuration(Duration duration) {
        long totalMinutes = duration.toMinutes();
        long days = totalMinutes / (24 * 60);
        long hours = (totalMinutes % (24 * 60)) / 60;
        long minutes = totalMinutes % 60;

        return String.format("%dD %dH %dM", days, hours, minutes);
    }
}

class LatLonDto {
    private String latitude;
    private String longitude;

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }
}

class TimeTakenAndDistDto {
    private String timeTaken;
    private String distance;

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public String getDistance() {
        return distance;
    }
}