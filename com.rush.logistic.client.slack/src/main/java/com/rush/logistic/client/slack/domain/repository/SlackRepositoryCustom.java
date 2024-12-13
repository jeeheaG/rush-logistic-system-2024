package com.rush.logistic.client.slack.domain.repository;

import com.rush.logistic.client.slack.domain.entity.SlackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SlackRepositoryCustom {
    Page<SlackEntity> findAll(Pageable pageable, int size);
    List<SlackEntity> findByMessage(String message);
    SlackEntity findBySlackId(Long slackId);
}
