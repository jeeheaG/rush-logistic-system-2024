package com.rush.logistic.client.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {

    private String userName;
    private String nickName;
}


//public class HubIdResponseDto {
//    private UUID hubId;
//
//    public static HubIdResponseDto from(UUID hubId) {
//        return HubIdResponseDto.builder()
//                .hubId(hubId)
//                .build();
//    }
//}
