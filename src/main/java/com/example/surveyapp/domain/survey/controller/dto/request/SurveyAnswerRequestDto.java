package com.example.surveyapp.domain.survey.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SurveyAnswerRequestDto {

    private List<QuestionAnswerRequestDto> answer;

}
