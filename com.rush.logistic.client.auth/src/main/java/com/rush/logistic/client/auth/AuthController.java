package com.rush.logistic.client.auth;

import com.rush.logistic.client.auth.core.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signIn")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody SignInRequest signInRequest){
        String token = authService.signIn(signInRequest.getUserId(), signInRequest.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/auth/signUp")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        User createdUser = authService.signUp(user);
        return ResponseEntity.ok(createdUser);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class AuthResponse {
        private String access_token;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class SignInRequest {

        private String userId;
        private String username;
        private String password;
        private String nickname;
        private String slackId;
        private String role;
    }
}