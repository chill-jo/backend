package com.example.surveyapp.domain.product.controller.dto;

import com.example.surveyapp.domain.product.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductUpdateRequestDto {

    private String title;

    private int price;

    private String content;

    private Status status;
}
