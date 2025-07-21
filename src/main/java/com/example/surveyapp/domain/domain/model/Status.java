package com.example.surveyapp.domain.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum Status {
    ON("판매중"),
    OFF("판매 중단");

    private String status;

}
