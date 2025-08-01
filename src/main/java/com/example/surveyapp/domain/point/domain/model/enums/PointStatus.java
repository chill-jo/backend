package com.example.surveyapp.domain.point.domain.model.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointStatus {
    DONE("완료"), FAILED("실패");

    private final String status;
}
