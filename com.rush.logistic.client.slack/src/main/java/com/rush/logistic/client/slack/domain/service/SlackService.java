package com.rush.logistic.client.slack.domain.service;

import com.rush.logistic.client.slack.domain.dto.SlackInfoResponseDto;
import com.rush.logistic.client.slack.domain.entity.BaseResponseDto;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Service
@Slf4j
@RequiredArgsConstructor
public class SlackService {

    @Value(value = "${slack.token}")
    String slackToken;

    private static final String SLACK_USER_INFO_URL = "https://slack.com/api/users.info";

    public String getSlackIdByEmail(String email) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + slackToken);

        String url = "https://slack.com/api/users.lookupByEmail?email=" + email;

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getBody() != null && response.getBody().contains("\"ok\":true")) {
            return extractSlackIdFromResponse(response.getBody());
        }
        throw new RuntimeException("Failed to fetch Slack ID for email: " + email);
    }

    private String extractSlackIdFromResponse(String response) {
        return response.split("\"id\":\"")[1].split("\"")[0];
    }


    public BaseResponseDto<SlackInfoResponseDto> sendSlackMessage(String message, String email) {

        String slackId = getSlackIdByEmail(email);
        String channelAddress;

        if (slackId == null || slackId.isEmpty()) {
            log.error("Slack ID를 찾을 수 없습니다. 이메일: " + email);
            return BaseResponseDto.error("Slack ID를 찾을 수 없습니다. 이메일: " + email, HttpStatus.NOT_FOUND.value());
        }

        channelAddress = slackId;

        try {
            MethodsClient methods = Slack.getInstance().methods(slackToken);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channelAddress)
                    .text(message)
                    .build();

            ChatPostMessageResponse response = methods.chatPostMessage(request);

            if (response.isOk()) {

                SlackInfoResponseDto slackInfoResponseDto = SlackInfoResponseDto.from(slackId,message);
                return BaseResponseDto.success(slackInfoResponseDto);
            } else {
                return BaseResponseDto.error("Slack 메시지 전송 실패: " + response.getError(), HttpStatus.BAD_REQUEST.value());
            }

        } catch (SlackApiException | IOException e) {
            return BaseResponseDto.error("Slack 메시지 전송 실패: " +  e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }
}
