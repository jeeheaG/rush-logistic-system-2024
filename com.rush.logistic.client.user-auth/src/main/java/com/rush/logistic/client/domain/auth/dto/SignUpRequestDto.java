package com.rush.logistic.client.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {

    @Size(min = 4, max = 10, message = "이름은 최소 4자 이상 10자 이하를 입력해주세요!")
    @Pattern(regexp = "^[a-z0-9]+$", message = "이름은 알파벳 소문자와 숫자만 입력 가능합니다!")
    @NotBlank(message = "이름을 입력해주세요!")
    private String username;

    @Size(min = 8, max = 15, message = "비밀번호는 최소 8자 이상 15자 이하를 입력해주세요!")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+=-]+$", message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다!")
    @NotBlank(message = "비밀번호를 입력해주세요!")
    private String password;

    @NotBlank(message = "슬랙ID를 입력해주세요!")
    private String slackId;
}
