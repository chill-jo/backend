package com.example.surveyapp.domain.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    On_SALE("판매중"),
    STOPED_SALE("판매 중단");

    private String status;
}
