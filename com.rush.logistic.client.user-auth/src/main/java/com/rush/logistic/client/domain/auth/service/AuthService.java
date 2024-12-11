package com.rush.logistic.client.domain.auth.service;

import com.rush.logistic.client.domain.auth.dto.SignInResponseDto;
import com.rush.logistic.client.domain.auth.dto.SignUpRequestDto;
import com.rush.logistic.client.domain.auth.dto.SignUpResponseDto;
import com.rush.logistic.client.domain.global.BaseResponseDto;
import com.rush.logistic.client.domain.user.entity.User;
import com.rush.logistic.client.domain.user.enums.UserRoleEnum;
import com.rush.logistic.client.domain.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(@Value("${service.jwt.secret-key}") String secretKey,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createAccessToken(Long userId, UserRoleEnum role) {
        return Jwts.builder()
                .claim("user_id", userId)
                .claim("role", role)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, io.jsonwebtoken.SignatureAlgorithm.HS512)
                .compact();
    }

    public BaseResponseDto<SignUpResponseDto> signUp(SignUpRequestDto signUpRequestDto) {

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        User user = User.builder()
                .username(signUpRequestDto.getUsername())
                .password(encodedPassword)
                .slackId(signUpRequestDto.getSlackId())
                .role(UserRoleEnum.MASTER)
                .build();

        user.setDelete(false);

        User savedUser = userRepository.save(user);

        SignUpResponseDto responseDto = SignUpResponseDto.of(savedUser);

        return BaseResponseDto.success(responseDto);
    }

    public BaseResponseDto<SignInResponseDto> signIn(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid user ID or password");
        }

        String token = createAccessToken(user.getUserId(), user.getRole());

        SignInResponseDto responseDto = SignInResponseDto.of(token);

        return BaseResponseDto.success(responseDto);

    }
}