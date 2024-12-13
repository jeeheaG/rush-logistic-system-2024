package com.rush.logistic.client.slack.domain.service;

import com.rush.logistic.client.domain.user.dto.UserInfoResponseDto;
import com.rush.logistic.client.domain.user.entity.User;
import com.rush.logistic.client.domain.user.enums.UserRoleEnum;
import com.rush.logistic.client.slack.domain.client.UserClient;
import com.rush.logistic.client.slack.domain.dto.SlackInfoListResponseDto;
import com.rush.logistic.client.slack.domain.dto.SlackInfoResponseDto;
import com.rush.logistic.client.slack.domain.entity.BaseResponseDto;
import com.rush.logistic.client.slack.domain.entity.SlackEntity;
import com.rush.logistic.client.slack.domain.repository.SlackRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlackService {

    private final SlackRepository slackRepository;
    private final UserClient userClient;

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
    public BaseResponseDto<SlackInfoResponseDto> sendSlackMessage(String userId,String username, String message, String email) {

        String slackId = getSlackIdByEmail(email);
        String channelAddress;
        log.info(username);
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



                SlackEntity slack = SlackEntity.builder()
                        .sendUserId(userId)
                        .receiveUserSlackId(slackId)
                        .message(message)
                        .build();

                // TODO : prepersist로 하는법?
                //  또는 인증객체 사용해서 자동으로 들어가게 수정 필요함.
                slack.setCreatedAt(LocalDateTime.now());
                slack.setCreatedBy(Long.parseLong(userId));

                slackRepository.save(slack);
                SlackInfoResponseDto slackInfoResponseDto = SlackInfoResponseDto.of(slack);

                return BaseResponseDto.success(slackInfoResponseDto);
            } else {
                return BaseResponseDto.error("Slack 메시지 전송 실패: " + response.getError(), HttpStatus.BAD_REQUEST.value());
            }

        } catch (SlackApiException | IOException e) {
            return BaseResponseDto.error("Slack 메시지 전송 실패: " +  e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }

    public BaseResponseDto<SlackInfoListResponseDto<SlackInfoResponseDto>> getAllSlacks(String userId, String role) {

//        User user = userClient.getUser(userId);
//        if(!Objects.equals(role, UserRoleEnum.MASTER.name())){
//
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponseDto.error("일치하지 않는 권한입니다.", HttpStatus.UNAUTHORIZED.value()));
//        }

        List<SlackInfoResponseDto> slackList = slackRepository.findAll().stream().map(SlackInfoResponseDto::of).collect(Collectors.toList());

        return  BaseResponseDto
                .success(SlackInfoListResponseDto.of(slackList));
    }

    public BaseResponseDto<SlackInfoResponseDto> getSlack(String slackId) {

        Optional<SlackEntity> slack = slackRepository.findById(Long.valueOf(slackId));

        if (slack.isEmpty()) {
            return BaseResponseDto.error("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value());
        }

        SlackInfoResponseDto slackInfoResponseDto = SlackInfoResponseDto.of(slack.get());
        return BaseResponseDto.success(slackInfoResponseDto);
    }
}