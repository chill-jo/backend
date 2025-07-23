package com.example.surveyapp.domain.product.controller.dto;

import com.example.surveyapp.domain.product.model.Product;
import com.example.surveyapp.domain.product.model.Status;
import lombok.Getter;

@Getter
public class ProductCreateResponseDto {

    private Long id;

    private String title;

    private int price;

    private Status status;

    public ProductCreateResponseDto(Product product) {
        this.id =product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.status = product.getStatus();
    }

    public ProductCreateResponseDto(Long id, String title, int price, Status status) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.status = status;
    }

    public static ProductCreateResponseDto from(Product product) {
        return new ProductCreateResponseDto(product);
    }
}
