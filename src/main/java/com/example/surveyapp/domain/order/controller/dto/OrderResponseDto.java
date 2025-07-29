package com.example.surveyapp.domain.order.controller.dto;

import com.example.surveyapp.domain.order.model.Order;
import com.example.surveyapp.domain.product.domain.model.Status;
import com.example.surveyapp.domain.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponseDto {

    private Long orderId;

    private Long userId;

    private String username;

    private String title;

    private Status status;

    private int price;

    public static OrderResponseDto from(Order order) {
    return new OrderResponseDto(
            order.getId(),
            order.getUser().getId(),
            order.getUser().getName(),
            order.getTitle(),
            order.getProduct().getStatus(),
            order.getPrice()
    );

    }



}
