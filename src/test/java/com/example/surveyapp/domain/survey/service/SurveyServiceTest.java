package com.example.surveyapp.domain.survey.service;

import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.survey.domain.repository.SurveyRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@DisplayName("Service::Survey")
@ExtendWith(MockitoExtension.class)
public class SurveyServiceTest {
    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private User user;

    @InjectMocks
    private SurveyService surveyService;

    @Test
    void 설문을_생성한다(){
        //Given
        String title = "테스트 설문 제목";
        String description = "테스트 설문 설명";
        Long maxSurveyee = 50L;
        Long pointPerPerson = 100L;
        LocalDateTime deadline = LocalDateTime.now();
        Long expectedTime = 20L;

        Survey surveyMock =




        //When

        //Then
    }

}
