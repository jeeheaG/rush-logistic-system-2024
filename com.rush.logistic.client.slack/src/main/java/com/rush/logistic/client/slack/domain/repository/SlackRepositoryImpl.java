package com.rush.logistic.client.slack.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rush.logistic.client.slack.domain.entity.QSlackEntity;
import com.rush.logistic.client.slack.domain.entity.SlackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.rush.logistic.client.slack.domain.entity.QSlackEntity.slackEntity;

public class SlackRepositoryImpl extends QuerydslRepositorySupport implements SlackRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public SlackRepositoryImpl(JPAQueryFactory queryFactory) {
        super(SlackEntity.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<SlackEntity> findAll(Pageable pageable, int size){

        pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

        JPAQuery<SlackEntity> query = queryFactory
                .select(
                        Projections.fields(
                                SlackEntity.class,
                                slackEntity.slackId,
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

        List<SlackEntity> slacks = getQuerydsl().applyPagination(pageable,query).fetch();

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
    public List<SlackEntity> findByMessage(String message) {

        return queryFactory
                .selectFrom(QSlackEntity.slackEntity)
                .where(
                        QSlackEntity.slackEntity.deletedAt.isNull()
                                .and(QSlackEntity.slackEntity.message.eq(message))
                )
                .fetch();
    }

    @Override
    public SlackEntity findBySlackId(Long slackId) {

        JPAQuery<SlackEntity> query = queryFactory
                .select(
                        Projections.fields(
                                SlackEntity.class,
                                slackEntity.slackId,
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
                .where(slackEntity.deletedAt.isNull()
                        .and(slackEntity.slackId.eq(slackId)));

        return query.fetchOne();
    }

}
