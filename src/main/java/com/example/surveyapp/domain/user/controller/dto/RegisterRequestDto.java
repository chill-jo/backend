package com.example.surveyapp.domain.user.controller.dto;

import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class RegisterRequestDto {
    @NotBlank
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String email;

    @NotBlank
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$",
            message = "비밀번호는 대소문자 영문자, 숫자, 특수문자를 각각 최소 1개 이상 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    String confirmPassword;

    @NotBlank
    @Size(min = 1, max = 10, message = "이름은 1~10자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "이름에는 숫자 또는 특수문자를 사용할 수 없습니다.")
    private String name;

    @NotBlank
    @Size(min = 1, max = 10, message = "별명은 1~10자여야 합니다.")
    private String nickname;

    @NotNull
    private UserRoleEnum userRoleEnum;

}
