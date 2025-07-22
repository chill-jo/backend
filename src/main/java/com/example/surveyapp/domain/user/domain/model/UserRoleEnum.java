package com.example.surveyapp.domain.user.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum {
    SURVEYEE("SURVEYEE", "설문 참여자"),
    SURVEYOR("SURVEYOR", "설문 출제자"),
    ADMIN("ADMIN", "관리자");

    private final String role;
    private final String description;
}
