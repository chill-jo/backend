package com.example.surveyapp.domain.survey.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
public enum SurveyStatus {
    NOT_STARTED("시작 전"),
    IN_PROGRESS("진행 중"),
    PAUSED("일시 중지"),
    DONE("종료");

    private final String status;

    SurveyStatus(String status){
        this.status = status;
    }

    public static SurveyStatus of(String input){
        return Arrays.stream(SurveyStatus.values())
                .filter(s -> s.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("설문 상태 오류"));
    }
}
