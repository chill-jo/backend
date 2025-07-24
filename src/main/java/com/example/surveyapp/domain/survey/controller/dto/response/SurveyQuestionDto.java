package com.example.surveyapp.domain.survey.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class SurveyQuestionDto {
    private Long id;

    private String title;

    private String description;

    private Long maxSurveyee;

    private Long pointPerPerson;

    private Long totalPoint;

    private LocalDateTime deadline;

    private Long expectedTime;

    private List<QuestionOptionsDto> questions;

    public void addQuestion(QuestionOptionsDto questionOptionsDto) {
        this.questions.add(questionOptionsDto);
    }
}