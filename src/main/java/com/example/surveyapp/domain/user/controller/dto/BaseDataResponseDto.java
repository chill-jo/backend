package com.example.surveyapp.domain.user.controller.dto;

import com.example.surveyapp.domain.user.domain.model.CategoryEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseDataResponseDto {

    private final CategoryEnum category;

    private final Long data;

}
