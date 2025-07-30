package com.example.surveyapp.domain.ai.moderation.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModerationResult {
    private final ModerationResultStatusEnum status;
    private final String reason;

    public boolean isApproved() {
        return status == ModerationResultStatusEnum.APPROVED;
    }

    public boolean isRejected() {
        return status == ModerationResultStatusEnum.DENIED;
    }
}
