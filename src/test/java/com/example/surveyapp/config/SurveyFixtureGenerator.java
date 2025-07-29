package com.example.surveyapp.config;

import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.user.domain.model.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class SurveyFixtureGenerator {
    public static final String title = "테스트 설문 제목";
    public static String description = "테스트 설문 설명";
    public static Long maxSurveyee = 50L;
    public static Long pointPerPerson = 100L;
    public static LocalDateTime deadline = LocalDateTime.of(2025, 7,25, 15,30);
    public static Long expectedTime = 20L;

    public static Survey generateSurveyFixture(){
        User user = UserFixtureGenerator.generateUserFixture();

        Survey survey = getSurvey(user, title, description, maxSurveyee, pointPerPerson, deadline, expectedTime);

        ReflectionTestUtils.setField(survey, "id", 1L);
//        ReflectionTestUtils.setField(survey, "totalPoint", maxSurveyee * pointPerPerson);
//        ReflectionTestUtils.setField(survey, "status", SurveyStatus.NOT_STARTED);
//        ReflectionTestUtils.setField(survey, "deleted", false);
        return survey;
    }

    public static Survey getSurvey(User user, String title, String description, Long maxSurveyee, Long pointPerPerson,
                                   LocalDateTime deadline, Long expectedTime){

        return Survey.of(user, title, description, maxSurveyee, pointPerPerson, deadline, expectedTime);
    }
}
