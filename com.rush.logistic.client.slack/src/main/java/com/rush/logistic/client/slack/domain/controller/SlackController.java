package com.rush.logistic.client.slack.domain.controller;


import com.rush.logistic.client.slack.domain.dto.SlackInfoResponseDto;
import com.rush.logistic.client.slack.domain.dto.SlackRequestDto;
import com.rush.logistic.client.slack.domain.entity.BaseResponseDto;
import com.rush.logistic.client.slack.domain.service.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class SlackController {

    private final SlackService slackService;

    @PostMapping("/slacks")
    public ResponseEntity<BaseResponseDto<SlackInfoResponseDto>> sendSlackMessage(@RequestHeader(value = "USER_ID", required = true) String userId,
                                                                                  @RequestBody SlackRequestDto slackRequestDto){

        BaseResponseDto<SlackInfoResponseDto> responseDto = slackService.sendSlackMessage(userId, slackRequestDto.getMessage(), slackRequestDto.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
