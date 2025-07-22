package com.example.surveyapp.domain.product.controller.dto;

import com.example.surveyapp.domain.product.model.Product;
import com.example.surveyapp.domain.product.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductCreateResponseDto {

    private String title;

    private int price;

    private Status status;

    public ProductCreateResponseDto(Product product) {
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.status = product.getStatus();
    }
}