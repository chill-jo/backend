package com.example.surveyapp.domain.product.controller.dto;

import com.example.surveyapp.domain.domain.model.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequestDto {

    @NotNull
    @Size(max = 30)
    private String title;

    @NotNull
    private String content;

    @NotNull
    private int price;

    @NotNull
    private Status status;

}
