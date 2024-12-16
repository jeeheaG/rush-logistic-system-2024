package com.rush.logistic.client.slack.domain.service;

import com.rush.logistic.client.slack.domain.client.UserClient;
import com.rush.logistic.client.slack.domain.client.UserResponseDto;
import com.rush.logistic.client.slack.domain.dto.SlackInfoResponseDto;
import com.rush.logistic.client.slack.domain.dto.SlackRequestDto;
import com.rush.logistic.client.slack.domain.dto.SlackUpdateRequestDto;
import com.rush.logistic.client.slack.domain.entity.SlackEntity;
import com.rush.logistic.client.slack.domain.global.ApiResponse;
import com.rush.logistic.client.slack.domain.global.exception.slack.NotFoundSlackException;
import com.rush.logistic.client.slack.domain.global.exception.slack.NotFoundSlackIdException;
import com.rush.logistic.client.slack.domain.global.exception.slack.SlackSendErrorException;
import com.rush.logistic.client.slack.domain.repository.SlackRepository;
import com.rush.logistic.client.slack.domain.repository.SlackRepositoryImpl;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlackService {

    private final SlackRepository slackRepository;
    private final UserClient userClient;
    private final SlackRepositoryImpl slackRepositoryImpl;

    @Value(value = "${slack.token}")
    String slackToken;


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

    @Transactional(readOnly = false)
    public SlackInfoResponseDto sendSlackMessage(String authenticatedUserId, String username, SlackRequestDto slackRequestDto) {

        String email = slackRequestDto.getEmail();
        String message = slackRequestDto.getMessage();

        String slackId = getSlackIdByEmail(email);

        String channelAddress;

        if (slackId == null || slackId.isEmpty()) {
            throw new NotFoundSlackIdException();
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

                SlackEntity slack = SlackEntity.builder()
                        .sendUserId(authenticatedUserId)
                        .receiveUserSlackId(slackId)
                        .message(message)
                        .build();

                slackRepository.save(slack);

                return SlackInfoResponseDto.from(slack);
            } else {
                throw new SlackSendErrorException();
            }

        } catch (SlackApiException | IOException e) {
            throw new SlackSendErrorException();
        }
    }

    public Page<SlackInfoResponseDto> getAllSlacks(String role, String authenticatedUserId, Pageable pageable, Integer size) {

        ApiResponse<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);

        return slackRepositoryImpl.findAll(pageable,size);
    }

    public SlackInfoResponseDto getSlack(String role, String authenticatedUserId, String slackId) {

        ApiResponse<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);

        return slackRepositoryImpl.findBySlackId(UUID.fromString(slackId));
    }

    @Transactional(readOnly = false)
    public SlackInfoResponseDto updateSlack(String role, String authenticatedUserId, String slackId, SlackUpdateRequestDto slackUpdateRequestDto) {

        ApiResponse<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);

        SlackEntity slackentity = slackRepository.findById(UUID.fromString(slackId)).orElseThrow(NotFoundSlackException::new);

        slackentity.updateSlackEntity(slackUpdateRequestDto);
        slackRepository.save(slackentity);

        return SlackInfoResponseDto.from(slackentity);
    }

    @Transactional(readOnly = false)
    public SlackInfoResponseDto deleteSlack(String role, String authenticatedUserId, String slackId) {

        ApiResponse<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);

        SlackEntity slackentity = slackRepository.findById(UUID.fromString(slackId)).orElseThrow(NotFoundSlackException::new);

        slackentity.setDelete(true);
        slackRepository.save(slackentity);

        return SlackInfoResponseDto.from(slackentity);
    }

    public List<SlackInfoResponseDto> getSlackByMessage(String role, String authenticatedUserId, String message) {

        ApiResponse<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);

        return slackRepositoryImpl.findByMessage(message);
    }
}












