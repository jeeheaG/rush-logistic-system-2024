package com.rush.logistic.client.order_delivery.global.util.gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
public class GeminiClient {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public String getExpectedStartTime(GeminiReq geminiReq) {
        String requestPrompt = geminiReq.toStringMessage();

        String requestBody = String.format(
                "{\"contents\": [{\"parts\": [{\"text\": \"%s\"}]}]}", requestPrompt
        );

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(requestBody, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl + "?key=" + apiKey, HttpMethod.POST, request, String.class);

        try{
            GeminiRes geminiRes = objectMapper.readValue(response.getBody(), GeminiRes.class);
            return geminiRes.getCandidates().get(0).getContent().getParts().get(0).getText();
        }catch (Exception e) {
            log.error("parse error : {}", e.getMessage());
            return "XXX cannot parse gemini response data XXX";
        }
    }



    // dto

    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GeminiRes {
        private List<Candidate> candidates;
        private UsageMetadata usageMetadata;
        private String modelVersion;
    }

    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Candidate {
        private Content content;
        private String finishReason;
        private Float avgLogprobs;
    }

    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Content {
        private List<Part> parts;
        private String role;
    }

    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Part {
        private String text;
    }

    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class UsageMetadata {
        private Integer promptTokenCount;
        private Integer candidatesTokenCount;
        private Integer totalTokenCount;
    }

}
