package com.example.surveyapp.config.generator;

import com.example.surveyapp.domain.survey.domain.model.entity.Question;
import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.survey.domain.model.enums.QuestionType;
import org.springframework.test.util.ReflectionTestUtils;

public class QuestionFixtureGenerator {

    public static Survey survey = SurveyFixtureGenerator.generateSurveyFixture();
    public static Long number = 1L;
    public static String content = "테스트질문내용";
    public static QuestionType type = QuestionType.SINGLE_CHOICE;

    public static Question generateQuestionFixture(){

        Question question = getQuestion(survey, number,content, type);

        ReflectionTestUtils.setField(question, "id", 1L);

        return question;
    }

    public static Question getQuestion(Survey survey, Long number, String content, QuestionType type){

        return Question.of(survey, number, content, type);
    }
}