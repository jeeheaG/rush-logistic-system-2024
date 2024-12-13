package com.rush.logistic.client.slack.domain.controller;


import com.rush.logistic.client.slack.domain.global.ApiResponse;
import com.rush.logistic.client.slack.domain.dto.SlackInfoResponseDto;
import com.rush.logistic.client.slack.domain.dto.SlackRequestDto;
import com.rush.logistic.client.slack.domain.service.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class SlackController {

    private final SlackService slackService;

    @PostMapping("/slacks")
    public ApiResponse<?> sendSlackMessage(@RequestHeader(value = "USER_ID", required = true) String userId,
                                           @RequestHeader(value = "USER_NAME", required = true) String username,
                                           @RequestBody SlackRequestDto slackRequestDto){

        return ApiResponse.ok(slackService.sendSlackMessage(userId, username, slackRequestDto));
    }

    @GetMapping("/slacks")
    public ApiResponse<?> getAllSlackMessages(@RequestHeader(value = "role", required = true) String role,
                                              @RequestHeader(value = "USER_ID", required = true) String userId,
                                              @PageableDefault(page = 0, size = 10, sort = "createdAt",
                                                      direction = Sort.Direction.DESC) Pageable pageable,
                                              @RequestParam Integer size){

        Page<SlackInfoResponseDto> slacks = slackService.getAllSlacks(role, userId, pageable, size);

        return ApiResponse.ok(slacks);
    }

    @GetMapping("/slacks/{slackId}")
    public ApiResponse<?> getSlackMessage(@RequestHeader(value = "role", required = true) String role,
                                          @RequestHeader(value = "USER_ID", required = true) String userId,
                                          @PathVariable String slackId) {

        return ApiResponse.ok(slackService.getSlack(role, userId, slackId));
    }
}
