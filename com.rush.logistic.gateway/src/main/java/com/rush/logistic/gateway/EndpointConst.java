package com.rush.logistic.gateway;

// TODO : gateway 에서 권한별 접근제한해주려면 어떤 방식을 쓰든 거의 모든 엔드포인트를 수기로 작성해야 하는데 이게 맞나요?? 이럴거면 각 서비스에서 security 쓰지
// -> 재고 필요
public class EndpointConst {
    public static final String USER_GET_ALL = "/api/users";
    public static final String USER_PATCH = "/api/users/";
}
