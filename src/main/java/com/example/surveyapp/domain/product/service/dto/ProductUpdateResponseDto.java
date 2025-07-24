package com.example.surveyapp.domain.product.service.dto;

import com.example.surveyapp.domain.product.model.Status;
import lombok.Getter;

@Getter
public class ProductUpdateResponseDto {

    private Long id;

    private String title;

    private String content;

    private int price;

    private Status status;

    public ProductUpdateResponseDto(Long id, String title, String content, int price, Status status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.price = price;
        this.status = status;
    }
}
