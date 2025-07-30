package com.example.surveyapp.domain.order.controller.dto;

import com.example.surveyapp.domain.order.model.Order;
import com.example.surveyapp.domain.product.domain.model.Status;
import com.example.surveyapp.domain.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderResponseDto {

    private Long orderId;

    private String orderNumber;

    private Long userId;

    private String username;

    private String title;

    private Long price;

    private Status status;

    private LocalDateTime createAt;


    public static OrderResponseDto from(Order order) {
    return new OrderResponseDto(
            order.getId(),
            order.getOrderNumber(),
            order.getUser().getId(),
            order.getUser().getName(),
            order.getTitle(),
            order.getPrice(),
            order.getProduct().getStatus(),
            order.getCreatedAt()

    );

    }



}
