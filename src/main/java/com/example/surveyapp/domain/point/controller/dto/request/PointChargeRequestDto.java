package com.example.surveyapp.domain.point.controller.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PointChargeRequestDto {

    @NotNull
    @Min(value=5000, message="최소 충전 금액은 5,000원 이상이어야 합니다.")
    private Long price;
}
