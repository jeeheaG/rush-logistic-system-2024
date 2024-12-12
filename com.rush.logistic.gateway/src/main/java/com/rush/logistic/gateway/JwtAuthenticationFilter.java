package com.rush.logistic.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    private static final String AUTH_ENDPOINT_PREFIX = "/api/auth";
    private static final String HEADER_USER_ID = "USER_ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("JwtAuthenticationFilter start");
        // 회원가입, 로그인일 경우 토큰 확인 안함
        String path = exchange.getRequest().getURI().getPath();
        if (path.startsWith(AUTH_ENDPOINT_PREFIX)) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);
        if (token == null || !isValidToken(token)) {
            log.info("JwtAuthenticationFilter : token is null or invalid");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // TODO : 헤더에 userId 만 남길 것인지, 토큰 그대로 전달할지
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));

        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build().parseSignedClaims(token);

        Claims claims = claimsJws.getBody();

        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("USER_ID", claims.get("user_id").toString())
                .header("USER_NAME", claims.get("user_name").toString())
                .header("role", claims.get("role").toString())
                .build();

        exchange = exchange.mutate().request(request).build();

        // 일단 subject 의 userid 추출해서 새 헤더 추가
//        String userId = getUserIdFromToken(token);
//        exchange.getRequest().getHeaders().add(HEADER_USER_ID, userId);

        return chain.filter(exchange);
    }

    /**
     * 토큰에서 userId 추출
     * @param token
     * @return
     */
    private String getUserIdFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build().parseSignedClaims(token);
        return claimsJws.getPayload().getSubject();
    }


    /**
     * 헤더에서 토큰값 추출하는 메서드
     * @param exchange
     * @return
     */
    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * 토큰값 검증 메서드
     * @param token
     * @return
     */
    private boolean isValidToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key)
                    .build().parseSignedClaims(token);
            log.info("#####payload :: " + claimsJws.getPayload().toString()); // TODO : 만료시간 찾아오기

            // 추가적인 검증 로직 여기에 추가
            // 1. 토큰 만료 여부 확인

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
