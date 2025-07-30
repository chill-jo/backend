package com.example.surveyapp.domain.product.controller.dto;

import com.example.surveyapp.domain.product.domain.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductUpdateRequestDto {

    private String title;

    private Long price;

    private String content;

    private Status status;
}
