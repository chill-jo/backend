package com.example.surveyapp.domain.survey.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.surveyapp.domain.survey.controller.dto.SurveyMapper;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyCreateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.response.SurveyResponseDto;
import com.example.surveyapp.domain.user.config.UserFixtureGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;

import com.example.surveyapp.config.SurveyFixtureGenerator;
import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.survey.domain.repository.SurveyRepository;
import com.example.surveyapp.domain.user.domain.model.User;

@DisplayName("Service::Survey")
@ExtendWith(MockitoExtension.class)
public class SurveyServiceTest {
    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private SurveyMapper surveyMapper;

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
        LocalDateTime deadline = LocalDateTime.of(2025, 7,25, 15,30);
        Long expectedTime = 20L;

        SurveyCreateRequestDto surveyCreateRequestDto = new SurveyCreateRequestDto(
                title, description, maxSurveyee, pointPerPerson, deadline, expectedTime);

        Survey surveyMock = SurveyFixtureGenerator.generateSurveyFixture();

        when(surveyMapper.createSurveyEntity(eq(surveyCreateRequestDto), eq(user)))
                .thenReturn(surveyMock);

        when(surveyRepository.save(any(Survey.class)))
                .thenReturn(surveyMock);

        when(surveyMapper.toResponseDto(eq(surveyMock)))
                .thenReturn(new SurveyResponseDto(
                        surveyMock.getId(),
                        surveyMock.getTitle(),
                        surveyMock.getDescription(),
                        surveyMock.getMaxSurveyee(),
                        surveyMock.getPointPerPerson(),
                        surveyMock.getTotalPoint(),
                        surveyMock.getDeadline(),
                        surveyMock.getExpectedTime(),
                        surveyMock.getStatus())
                );

        //When
        SurveyResponseDto surveyResponseDto = surveyService.createSurvey(surveyCreateRequestDto, user);

        //Then
        assertThat(surveyResponseDto.getTitle()).isEqualTo(title);
        assertThat(surveyResponseDto.getDescription()).isEqualTo(description);
        assertThat(surveyResponseDto.getMaxSurveyee()).isEqualTo(maxSurveyee);
        assertThat(surveyResponseDto.getPointPerPerson()).isEqualTo(pointPerPerson);
        assertThat(surveyResponseDto.getDeadline()).isEqualTo(deadline);
        assertThat(surveyResponseDto.getExpectedTime()).isEqualTo(expectedTime);

        verify(surveyRepository, times(1))
                .save(any(Survey.class));
    }

    @Test
    void 설문_목록을_조회한다(){
        //Given

        //When

        //Then
    }
}
