package com.example.surveyapp.domain.survey.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.surveyapp.domain.survey.controller.dto.SurveyMapper;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyCreateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyStatusUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.response.PageSurveyResponseDto;
import com.example.surveyapp.domain.survey.controller.dto.response.SurveyResponseDto;
import com.example.surveyapp.domain.survey.controller.dto.response.SurveyStatusResponseDto;
import com.example.surveyapp.domain.survey.domain.model.enums.SurveyStatus;
import com.example.surveyapp.domain.survey.facade.UserFacade;
import com.example.surveyapp.global.response.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.surveyapp.config.SurveyFixtureGenerator;
import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.survey.domain.repository.SurveyRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("Service::Survey")
@ExtendWith(MockitoExtension.class)
public class SurveyServiceTest {
    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private SurveyMapper surveyMapper;

    @Mock
    private UserFacade userFacade;

    @InjectMocks
    private SurveyService surveyService;

    @Test
    void 설문을_생성한다(){
        //Given
        User userMock = mock(User.class);
        Long userId = 1L;

        String title = "테스트 설문 제목";
        String description = "테스트 설문 설명";
        Long maxSurveyee = 50L;
        Long pointPerPerson = 100L;
        LocalDateTime deadline = LocalDateTime.of(2025, 7,25, 15,30);
        Long expectedTime = 20L;

        SurveyCreateRequestDto surveyCreateRequestDto = new SurveyCreateRequestDto(
                title, description, maxSurveyee, pointPerPerson, deadline, expectedTime);

        Survey surveyMock = SurveyFixtureGenerator.generateSurveyFixture();
        Survey saved = SurveyFixtureGenerator.generateSurveyFixture();

        when(surveyMapper.createSurveyEntity(surveyCreateRequestDto, userMock))
                .thenReturn(surveyMock);

        when(surveyRepository.save(surveyMock))
                .thenReturn(saved);

        when(surveyMapper.toResponseDto(saved))
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
        SurveyResponseDto surveyResponseDto = surveyService.createSurvey(userId, surveyCreateRequestDto);

        //Then
        assertThat(surveyResponseDto.getTitle()).isEqualTo(title);
        assertThat(surveyResponseDto.getDescription()).isEqualTo(description);
        assertThat(surveyResponseDto.getMaxSurveyee()).isEqualTo(maxSurveyee);
        assertThat(surveyResponseDto.getPointPerPerson()).isEqualTo(pointPerPerson);
        assertThat(surveyResponseDto.getDeadline()).isEqualTo(deadline);
        assertThat(surveyResponseDto.getExpectedTime()).isEqualTo(expectedTime);

        verify(userMock).isUserRoleSurveyee();
        verify(surveyMapper).createSurveyEntity(surveyCreateRequestDto, userMock);
        verify(surveyRepository, times(1))
                .save(surveyMock);
        verify(surveyMapper).toResponseDto(saved);
    }

    @Test
    void 참여자가_설문_출제_요청을_보낼때_에러가_발생한다(){
        User userMock = mock(User.class);
        Long userId = 1L;
        String title = "테스트 설문 제목";
        String description = "테스트 설문 설명";
        Long maxSurveyee = 50L;
        Long pointPerPerson = 100L;
        LocalDateTime deadline = LocalDateTime.of(2025, 7,25, 15,30);
        Long expectedTime = 20L;

        SurveyCreateRequestDto surveyCreateRequestDto = new SurveyCreateRequestDto(
                title, description, maxSurveyee, pointPerPerson, deadline, expectedTime);

        when(userMock.isUserRoleSurveyee()).thenReturn(true);

        //then
        assertThatThrownBy(() -> surveyService.createSurvey(userId, surveyCreateRequestDto))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("참여자 권한으로는 질문을 생성할 수 없습니다");

        verify(userMock).isUserRoleSurveyee();
    }

    @Test
    void 설문_목록을_조회한다(){
        //Given
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);

        Survey surveyMock1 = SurveyFixtureGenerator.generateSurveyFixture();
        Survey surveyMock2 = SurveyFixtureGenerator.generateSurveyFixture();
        ReflectionTestUtils.setField(surveyMock2, "id", 2L);

        List<Survey> surveyList = List.of(surveyMock1, surveyMock2);
        Page<Survey> surveyPage = new PageImpl<>(surveyList, pageable, surveyList.size());

        when(surveyRepository.findAllSurveyPaged(pageable)).thenReturn(surveyPage);

        when(surveyMapper.toResponseDto(surveyMock1))
                .thenReturn(new SurveyResponseDto(
                        surveyMock1.getId(),
                        surveyMock1.getTitle(),
                        surveyMock1.getDescription(),
                        surveyMock1.getMaxSurveyee(),
                        surveyMock1.getPointPerPerson(),
                        surveyMock1.getTotalPoint(),
                        surveyMock1.getDeadline(),
                        surveyMock1.getExpectedTime(),
                        surveyMock1.getStatus()
        ));
        when(surveyMapper.toResponseDto(surveyMock2))
                .thenReturn(new SurveyResponseDto(
                        surveyMock2.getId(),
                        surveyMock2.getTitle(),
                        surveyMock2.getDescription(),
                        surveyMock2.getMaxSurveyee(),
                        surveyMock2.getPointPerPerson(),
                        surveyMock2.getTotalPoint(),
                        surveyMock2.getDeadline(),
                        surveyMock2.getExpectedTime(),
                        surveyMock2.getStatus()
                ));

        //When

        PageSurveyResponseDto<SurveyResponseDto> pageResponseDto = surveyService.getSurveys(page, size);

        //Then

        assertThat(pageResponseDto).isNotNull();
        assertThat(pageResponseDto.getContent()).hasSize(2);

        assertThat(pageResponseDto.getContent().get(0).getId()).isEqualTo(surveyMock1.getId());
        assertThat(pageResponseDto.getContent().get(1).getId()).isEqualTo(surveyMock2.getId());
        assertThat(pageResponseDto.getTotalElements()).isEqualTo(surveyList.size());

        verify(surveyRepository, times(1))
                .findAllSurveyPaged(pageable);
        verify(surveyMapper, times(1)).toResponseDto(surveyMock1);
        verify(surveyMapper, times(1)).toResponseDto(surveyMock2);

    }

    @Test
    void 설문_출제자가_설문_상세정보를_수정한다(){
        //given
        User userMock = mock(User.class);
        Long userId = 1L;
        Long id = 1L;
        String title = "테스트설문제목수정";
        String description = "테스트설문내용수정";
        Long maxSurveyee = 100L;
        Long pointPerPerson = 100L;
        LocalDateTime deadline = LocalDateTime.of(2025, 7,25, 15,30);
        Long expectedTime = 20L;

        SurveyUpdateRequestDto updateRequestDto = new SurveyUpdateRequestDto(
                title, description, maxSurveyee, pointPerPerson, deadline, expectedTime);

        Survey surveyMock = mock(Survey.class);

        when(surveyRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(surveyMock));

        when(userMock.isUserRoleSurveyee()).thenReturn(false);
        when(surveyMock.isUserSurveyCreator(userMock)).thenReturn(true);
        when(surveyMock.isNotStarted()).thenReturn(true);

        doNothing().when(surveyMapper).updateSurvey(updateRequestDto, surveyMock);

        when(surveyRepository.save(surveyMock)).thenReturn(surveyMock);

        SurveyResponseDto expectedResponseDto = new SurveyResponseDto(
                id,
               title,
                description,
                maxSurveyee,
                pointPerPerson,
                maxSurveyee * pointPerPerson,
                deadline,
                expectedTime,
                SurveyStatus.NOT_STARTED
        );

        when(surveyMapper.toResponseDto(surveyMock)).thenReturn(expectedResponseDto);

        //when
        SurveyResponseDto responseDto = surveyService.updateSurvey(userId, 1L, updateRequestDto);

        //then
        assertThat(responseDto.getTitle()).isEqualTo(title);
        assertThat(responseDto.getDescription()).isEqualTo(description);
        assertThat(responseDto.getMaxSurveyee()).isEqualTo(maxSurveyee);
        assertThat(responseDto.getTotalPoint()).isEqualTo(maxSurveyee * pointPerPerson);

        verify(surveyRepository).findByIdAndIsDeletedFalse(id);
        verify(userMock).isUserRoleSurveyee();
        verify(surveyMock).isUserSurveyCreator(userMock);
        verify(userMock, never()).isUserRoleNotAdmin();
        verify(surveyMock).isNotStarted();
        verify(surveyMapper).updateSurvey(updateRequestDto, surveyMock);
        verify(surveyRepository).save(surveyMock);
        verify(surveyMapper).toResponseDto(surveyMock);

    }

    @Test
    void 관리자가_설문_상태를_변경한다(){
        //given
        User userMock = mock(User.class);
        Long userId = 1L;
        Long id = 1L;
        SurveyStatus status = SurveyStatus.IN_PROGRESS;

        SurveyStatusUpdateRequestDto updateRequestDto = new SurveyStatusUpdateRequestDto(status);

        Survey surveyMock = mock(Survey.class);

        when(surveyRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(surveyMock));

        when(userMock.isUserRoleSurveyee()).thenReturn(false);
        when(surveyMock.isUserSurveyCreator(userMock)).thenReturn(false);
        when(userMock.isUserRoleNotAdmin()).thenReturn(false);

        when(surveyMock.getStatus()).thenReturn(SurveyStatus.NOT_STARTED)
                .thenReturn(status);

        doNothing().when(surveyMock).changeSurveyStatus(status);
        when(surveyRepository.save(surveyMock)).thenReturn(surveyMock);

        //when
        SurveyStatusResponseDto responseDto = surveyService.updateSurveyStatus(userId, id, updateRequestDto);

        //then

        assertThat(responseDto.getStatus()).isEqualTo(status);

        verify(surveyRepository).findByIdAndIsDeletedFalse(id);
        verify(userMock).isUserRoleSurveyee();
        verify(userMock).isUserRoleNotAdmin();
        verify(surveyMock).isUserSurveyCreator(userMock);
        verify(surveyMock, times(2)).getStatus();
        verify(surveyMock).changeSurveyStatus(status);
        verify(surveyRepository).save(surveyMock);

    }

    @Test
    void 관리자가_설문_상태를_진행중에서_진행전으로_변경할시_에러가_발생한다(){
        //given
        User userMock = mock(User.class);
        Long userId = 1L;
        Long id = 1L;
        SurveyStatus status = SurveyStatus.NOT_STARTED;

        SurveyStatusUpdateRequestDto requestDto = new SurveyStatusUpdateRequestDto(status);

        Survey surveyMock = mock(Survey.class);

        when(surveyRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(surveyMock));

        when(userMock.isUserRoleSurveyee()).thenReturn(false);
        when(surveyMock.isUserSurveyCreator(userMock)).thenReturn(false);
        when(userMock.isUserRoleNotAdmin()).thenReturn(false);

        when(surveyMock.getStatus()).thenReturn(SurveyStatus.IN_PROGRESS);

//        doNothing().when(surveyMock).changeSurveyStatus(status);
//        when(surveyRepository.save(surveyMock)).thenReturn(surveyMock);

        //when
        //then
        assertThatThrownBy(() -> surveyService.updateSurveyStatus(userId, id, requestDto))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("설문은 진행 전 상태로 변경할 수 없습니다");

        verify(surveyRepository).findByIdAndIsDeletedFalse(id);
        verify(userMock).isUserRoleSurveyee();
        verify(userMock).isUserRoleNotAdmin();
        verify(surveyMock).isUserSurveyCreator(userMock);
        verify(surveyMock, times(1)).getStatus();
        verify(surveyMock, never()).changeSurveyStatus(status);
        verify(surveyRepository, never()).save(surveyMock);


    }

    @Test
    void 설문을_삭제한다(){
        //given
        User userMock = mock(User.class);
        Long userId = 1L;
        Long id = 1L;

        Survey surveyMock = mock(Survey.class);

        when(surveyRepository.findById(id)).thenReturn(Optional.of(surveyMock));

        when(userMock.isUserRoleSurveyee()).thenReturn(false);
        when(surveyMock.isUserSurveyCreator(userMock)).thenReturn(true);

        when(surveyMock.isDeleted()).thenReturn(false);

        when(surveyMock.isInProgress()).thenReturn(false);

        doNothing().when(surveyMock).deleteSurvey();

        //when
        surveyService.deleteSurvey(userId, id);

        //then
        verify(surveyRepository).findById(id);
        verify(userMock).isUserRoleSurveyee();
        verify(surveyMock).isUserSurveyCreator(userMock);
        verify(surveyMock).isDeleted();
        verify(surveyMock).isInProgress();
        verify(surveyMock).deleteSurvey();

    }

}
