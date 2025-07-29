package com.example.surveyapp.domain.survey.controller.dto.response;

import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public static SurveyQuestionDto of(Survey survey) {
        return new SurveyQuestionDto(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                survey.getMaxSurveyee(),
                survey.getPointPerPerson(),
                survey.getTotalPoint(),
                survey.getDeadline(),
                survey.getExpectedTime(),
                new ArrayList<>()
        );
    }
}
