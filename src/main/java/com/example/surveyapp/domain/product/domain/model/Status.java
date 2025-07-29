package com.example.surveyapp.domain.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    ON_SALE("판매중"),
    STOPPED_SALE("판매 중단");

    private String status;
}
