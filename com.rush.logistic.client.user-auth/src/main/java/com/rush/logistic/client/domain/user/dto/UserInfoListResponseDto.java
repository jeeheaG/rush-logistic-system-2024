package com.rush.logistic.client.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoListResponseDto<T> {
    private List<T> userList;

    public static <T> UserInfoListResponseDto<T> of(List<T> userList) {
        return UserInfoListResponseDto.<T>builder()
                .userList(userList)
                .build();
    }
}