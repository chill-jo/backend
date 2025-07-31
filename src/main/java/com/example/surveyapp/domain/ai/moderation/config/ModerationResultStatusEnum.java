package com.example.surveyapp.domain.ai.moderation.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModerationResultStatusEnum {
    APPROVED,
    DENIED;
}