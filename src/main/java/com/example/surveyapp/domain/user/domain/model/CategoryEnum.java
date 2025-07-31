package com.example.surveyapp.domain.user.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryEnum {

    GENDER("성별", "1) 남자    2) 여자", 2L),
    AGE("연령대", "1) 10대    2) 20대    3) 30대    4) 40대    5) 50대    6) 60대", 6L),
    REGIDENCE("거주지역", "1) 경기/수도권    2) 강원권    3) 충청권    4) 제주/전라권    5) 경상권", 5L);

    private final String category;
    private final String description;
    private final Long optionMaxNum;

}
