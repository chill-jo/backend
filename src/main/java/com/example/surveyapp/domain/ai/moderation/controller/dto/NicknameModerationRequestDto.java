package com.example.surveyapp.domain.ai.moderation.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NicknameModerationRequestDto {
    @NotBlank
    private String nickname;
}
