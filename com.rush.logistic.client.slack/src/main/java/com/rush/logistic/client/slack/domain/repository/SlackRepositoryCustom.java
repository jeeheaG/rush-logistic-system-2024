package com.rush.logistic.client.slack.domain.repository;

import com.rush.logistic.client.slack.domain.dto.SlackInfoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SlackRepositoryCustom {
    Page<SlackInfoResponseDto> findAll(Pageable pageable, int size);
    List<SlackInfoResponseDto> findByMessage(String message);
    SlackInfoResponseDto findBySlackId(UUID slackId);
}
