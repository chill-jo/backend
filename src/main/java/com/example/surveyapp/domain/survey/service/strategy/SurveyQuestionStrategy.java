package com.example.surveyapp.domain.survey.service.strategy;

import com.example.surveyapp.domain.survey.controller.dto.request.QuestionAnswerRequestDto;
import com.example.surveyapp.domain.survey.domain.model.entity.Question;
import com.example.surveyapp.domain.survey.domain.model.entity.SurveyAnswer;
import com.example.surveyapp.domain.survey.domain.model.enums.QuestionType;

public interface SurveyQuestionStrategy {
    void doSave(QuestionAnswerRequestDto questionAnswer, SurveyAnswer surveyAnswer, Question question);

    boolean isSupport(QuestionType questionType);
}
