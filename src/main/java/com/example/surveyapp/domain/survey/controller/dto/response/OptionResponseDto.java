package com.example.surveyapp.domain.survey.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionResponseDto {

    private Long id;

    private Long number;

    private String content;
}
