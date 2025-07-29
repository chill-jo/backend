package com.example.surveyapp.domain.user.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryEnum {

    GENDER("성별"),
    AGE("연령대"),
    REGIDENCE("거주지역");

    private final String description;

}
