package com.rush.logistic.client.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rush.logistic.client.domain.user.dto.UserInfoResponseDto;
import com.rush.logistic.client.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.rush.logistic.client.domain.user.entity.QUser.user;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<UserInfoResponseDto> findAll(Pageable pageable, int size){

        pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

        JPAQuery<UserInfoResponseDto> query = queryFactory
                .select(
                        Projections.fields(
                                UserInfoResponseDto.class,
                                user.userId,
                                user.username,
                                user.password,
                                user.role,
                                user.email,
                                user.hubId,
                                user.companyId,
                                user.createdAt,
                                user.createdBy,
                                user.updatedAt,
                                user.updatedBy
                        )
                )
                .from(user)
                .where(user.deletedAt.isNull());

        List<UserInfoResponseDto> users = getQuerydsl().applyPagination(pageable,query).fetch();

        long totalCount = users.size();

        return new PageImpl<>(users, pageable, totalCount);
    }
}
