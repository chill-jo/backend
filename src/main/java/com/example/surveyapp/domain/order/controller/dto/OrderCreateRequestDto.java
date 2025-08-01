package com.example.surveyapp.domain.order.controller.dto;

import com.example.surveyapp.domain.user.domain.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreateRequestDto {

    @NotNull
    private Long productId;

 }
