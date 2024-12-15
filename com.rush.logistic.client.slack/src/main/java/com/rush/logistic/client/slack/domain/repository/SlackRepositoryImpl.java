package com.rush.logistic.client.slack.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rush.logistic.client.slack.domain.dto.SlackInfoResponseDto;
import com.rush.logistic.client.slack.domain.entity.SlackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.UUID;

import static com.rush.logistic.client.slack.domain.entity.QSlackEntity.slackEntity;

public class SlackRepositoryImpl extends QuerydslRepositorySupport implements SlackRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public SlackRepositoryImpl(JPAQueryFactory queryFactory) {
        super(SlackEntity.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<SlackInfoResponseDto> findAll(Pageable pageable, int size){

        pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

        JPAQuery<SlackInfoResponseDto> query = queryFactory
                .select(
                        Projections.fields(
                                SlackInfoResponseDto.class,
                                slackEntity.id,
                                slackEntity.message,
                                slackEntity.sendUserId,
                                slackEntity.receiveUserSlackId,
                                slackEntity.createdAt,
                                slackEntity.createdBy,
                                slackEntity.updatedAt,
                                slackEntity.updatedBy
                        )
                )
                .from(slackEntity)
                .where(slackEntity.deletedAt.isNull());

        List<SlackInfoResponseDto> slacks = getQuerydsl().applyPagination(pageable,query).fetch();

        long totalCount = slacks.size();

        return new PageImpl<>(slacks, pageable, totalCount);
    }

//    @Cacheable("slackEntityCount")
//    public long getTotalCount() {
//        return queryFactory
//                .select(slackEntity.slackId.count())
//                .from(slackEntity)
//                .where(slackEntity.deletedAt.isNull())
//                .fetchOne();
//    }

    @Override
    public List<SlackInfoResponseDto> findByMessage(String message) {
        return queryFactory
                .select(
                        Projections.fields(
                                SlackInfoResponseDto.class,
                                slackEntity.id,
                                slackEntity.message,
                                slackEntity.sendUserId,
                                slackEntity.receiveUserSlackId,
                                slackEntity.createdAt,
                                slackEntity.createdBy,
                                slackEntity.updatedAt,
                                slackEntity.updatedBy
                        )
                )
                .from(slackEntity)
                .where(
                        slackEntity.deletedAt.isNull()
                                .and(slackEntity.message.eq(message))
                )
                .fetch();
    }

    @Override
    public SlackInfoResponseDto findBySlackId(UUID slackId) {
        return queryFactory
                .select(
                        Projections.fields(
                                SlackInfoResponseDto.class,
                                slackEntity.id,
                                slackEntity.message,
                                slackEntity.sendUserId,
                                slackEntity.receiveUserSlackId,
                                slackEntity.createdAt,
                                slackEntity.createdBy,
                                slackEntity.updatedAt,
                                slackEntity.updatedBy
                        )
                )
                .from(slackEntity)
                .where(
                        slackEntity.deletedAt.isNull()
                                .and(slackEntity.id.eq(slackId))
                )
                .fetchOne();
    }

}
