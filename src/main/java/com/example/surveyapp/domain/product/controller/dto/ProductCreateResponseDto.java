package com.example.surveyapp.domain.product.controller.dto;

import com.example.surveyapp.domain.product.domain.model.Product;
import com.example.surveyapp.domain.product.domain.model.Status;
import lombok.Getter;

@Getter
public class ProductCreateResponseDto {

    private Long id;

    private String title;

    private Long price;

    private Status status;

    public ProductCreateResponseDto(Product product) {
        this.id =product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.status = product.getStatus();
    }

    public ProductCreateResponseDto(Long id, String title, Long price, Status status) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.status = status;
    }

    public static ProductCreateResponseDto from(Product product) {
        return new ProductCreateResponseDto(product);
    }
}
