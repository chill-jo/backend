package com.example.surveyapp.domain.order.controller.dto;

import com.example.surveyapp.domain.order.model.Order;
import com.example.surveyapp.domain.product.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreateResponseDto {

    private Long id;

    private String title;

    private Status status;

    private int price;

    public static OrderCreateResponseDto from(Order order) {
        return new OrderCreateResponseDto(
                order.getId(),
                order.getTitle(),
                order.getProduct().getStatus(),
                order.getPrice());
    }
}
