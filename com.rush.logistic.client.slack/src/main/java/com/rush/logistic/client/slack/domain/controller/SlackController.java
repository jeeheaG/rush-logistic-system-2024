package com.rush.logistic.client.slack.domain.controller;


import com.rush.logistic.client.slack.domain.dto.SlackInfoResponseDto;
import com.rush.logistic.client.slack.domain.dto.SlackRequestDto;
import com.rush.logistic.client.slack.domain.entity.BaseResponseDto;
import com.rush.logistic.client.slack.domain.service.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class SlackController {

    private final SlackService slackService;

    @PostMapping("/slacks")
    public ResponseEntity<BaseResponseDto<SlackInfoResponseDto>> sendSlackMessage(@RequestBody SlackRequestDto slackRequestDto){

        BaseResponseDto<SlackInfoResponseDto> responseDto = slackService.sendSlackMessage(slackRequestDto.getMessage(), slackRequestDto.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
