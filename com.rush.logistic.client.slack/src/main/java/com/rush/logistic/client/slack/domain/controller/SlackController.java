package com.rush.logistic.client.slack.domain.controller;


import com.rush.logistic.client.domain.user.enums.UserRoleEnum;
import com.rush.logistic.client.slack.domain.dto.SlackInfoListResponseDto;
import com.rush.logistic.client.slack.domain.dto.SlackInfoResponseDto;
import com.rush.logistic.client.slack.domain.dto.SlackRequestDto;
import com.rush.logistic.client.slack.domain.entity.BaseResponseDto;
import com.rush.logistic.client.slack.domain.service.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class SlackController {

    private final SlackService slackService;

    @PostMapping("/slacks")
    public ResponseEntity<BaseResponseDto<SlackInfoResponseDto>> sendSlackMessage(@RequestHeader(value = "USER_ID", required = true) String userId,
                                                                                  @RequestHeader(value = "USER_NAME", required = true) String username,
                                                                                  @RequestBody SlackRequestDto slackRequestDto){

        BaseResponseDto<SlackInfoResponseDto> responseDto = slackService.sendSlackMessage(userId,username, slackRequestDto.getMessage(), slackRequestDto.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/slacks")
    public ResponseEntity<BaseResponseDto<SlackInfoListResponseDto<SlackInfoResponseDto>>> getSlackMessage(@RequestHeader(value = "role", required = true) String role                                                                                                 ){

        if(!Objects.equals(role, UserRoleEnum.MASTER.name())){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponseDto.error("일치하지 않는 권한입니다.", HttpStatus.UNAUTHORIZED.value()));
        }

        BaseResponseDto<SlackInfoListResponseDto<SlackInfoResponseDto>> responseDto = slackService.getAllSlacks();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
