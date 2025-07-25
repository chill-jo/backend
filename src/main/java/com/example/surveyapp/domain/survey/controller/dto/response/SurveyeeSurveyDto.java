package com.example.surveyapp.domain.survey.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class SurveyeeSurveyDto {

    private Long surveyId;

    private String title;

    private LocalDateTime date;
}
