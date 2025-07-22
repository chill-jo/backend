package com.example.surveyapp.domain.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    On_sale("판매중"),
    Discontinued_sale("판매 중단");

    private String status;

}
