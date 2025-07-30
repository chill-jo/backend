package com.example.surveyapp.domain.product.service.dto;

import com.example.surveyapp.domain.product.domain.model.Status;
import lombok.Getter;

@Getter
public class ProductUpdateResponseDto {

    private Long id;

    private String title;

    private String content;

    private Long price;

    private Status status;

    public ProductUpdateResponseDto(Long id, String title, String content, Long price, Status status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.price = price;
        this.status = status;
    }
}
