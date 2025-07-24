package com.example.surveyapp.domain.point.domain.model.enums;


import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum PaymentType {
    POINT_CHARGE("포인트 충전");

    private final String type;
}