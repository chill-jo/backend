package com.example.surveyapp.domain.survey.service;

import static org.assertj.core.api.Assertions.*;
import com.example.surveyapp.config.QuestionFixtureGenerator;
import com.example.surveyapp.domain.survey.controller.dto.request.QuestionCreateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.QuestionUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.response.QuestionResponseDto;
import com.example.surveyapp.domain.survey.domain.model.entity.Question;
import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.survey.domain.model.enums.QuestionType;
import com.example.surveyapp.domain.survey.domain.repository.QuestionRepository;
import com.example.surveyapp.domain.survey.domain.repository.SurveyRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.swing.text.html.Option;
import java.sql.Ref;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("Service::Question")
@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private QuestionService questionService;
    @Captor
    ArgumentCaptor<Question> questionCaptor;
    @Test
    void 질문을_생성한다(){
        Long questionId = 1L;
        Long userId = 1L;
        Long surveyId = 1L;
        Long number = 1L;
        String content = "테스트질문내용";
        QuestionType type = QuestionType.SINGLE_CHOICE;

        User userMock = mock(User.class);
        Survey surveyMock = mock(Survey.class);

        QuestionCreateRequestDto requestDto = new QuestionCreateRequestDto(number, content, type);

        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(surveyMock));
        when(userMock.isUserRoleSurveyee()).thenReturn(false);
        when(surveyMock.isUserSurveyCreator(userMock)).thenReturn(true);
        when(surveyMock.isSurveyStatusNotStarted()).thenReturn(true);

        // question을 생성자로 만들어서 저장하고 있기 때문에
        // fixture를 전달해 줄 수 없어서 fixture generator 사용할 수 없다
        // 생성자로 만들어진 question 객체가 인자로 전달되고, survey, number,content, type 필드는 채워진다.
        // 그렇지만 jpa가 실행되지 않기 때문에 id값은 안생김.
        // 그래서 id 값 직접 세팅
        when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> {
            Question q = invocation.getArgument(0);
            ReflectionTestUtils.setField(q, "id", 1L);
            return q;
        });

        //when
        QuestionResponseDto responseDto = questionService.createQuestion(userMock, surveyId, requestDto);

        //then
        verify(questionRepository).save(questionCaptor.capture());

        Question savedQuestion = questionCaptor.getValue();
        assertThat(savedQuestion.getSurvey()).isSameAs(surveyMock);

        assertThat(responseDto.getId()).isEqualTo(questionId);
        assertThat(responseDto.getNumber()).isEqualTo(number);
        assertThat(responseDto.getContent()).isEqualTo(content);
        assertThat(responseDto.getType()).isEqualTo(type);
        verify(surveyRepository).findById(surveyId);
        verify(userMock).isUserRoleSurveyee();
        verify(surveyMock).isUserSurveyCreator(userMock);
        verify(surveyMock).isSurveyStatusNotStarted();
        verify(questionRepository).save(any(Question.class));
    }


    @Test
    void 질문이_진행중이_아닐때_설문출제자가_단건_조회한다(){
        Long userId = 1L;
        Long surveyId = 1L;
        Long questionId = 1L;
        Long number = 1L;
        String content = "테스트질문내용";
        QuestionType type = QuestionType.SINGLE_CHOICE;

        Survey surveyMock = mock(Survey.class);
        User userMock = mock(User.class);
        Question questionMock = mock(Question.class);

        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(surveyMock));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(questionMock));

        when(questionMock.isFromSurvey(surveyMock)).thenReturn(true);

        when(surveyMock.isSurveyStatusInProgress()).thenReturn(false);
        when(userMock.isUserRoleSurveyee()).thenReturn(false);
        when(surveyMock.isUserSurveyCreator(userMock)).thenReturn(true);
        when(questionMock.getId()).thenReturn(questionId);
        when(questionMock.getNumber()).thenReturn(number);
        when(questionMock.getContent()).thenReturn(content);
        when(questionMock.getType()).thenReturn(type);

        //when
        QuestionResponseDto responseDto = questionService.getQuestion(userMock, surveyId, questionId);

        //then
        assertThat(responseDto.getId()).isEqualTo(questionId);
        assertThat(responseDto.getNumber()).isEqualTo(number);
        assertThat(responseDto.getContent()).isEqualTo(content);
        assertThat(responseDto.getType()).isEqualTo(type);

        verify(surveyRepository).findById(surveyId);
        verify(questionRepository).findById(questionId);
        verify(questionMock).isFromSurvey(surveyMock);
        verify(surveyMock).isSurveyStatusInProgress();
        verify(userMock).isUserRoleSurveyee();
        verify(surveyMock).isUserSurveyCreator(userMock);

    }

    @Test
    void 관리자가_질문을_수정한다(){
        User userMock = mock(User.class);
        Long surveyId = 1L;
        Long questionId = 1L;
        Long number = 2L;
        String content = "테스트질문내용수정";

        Survey surveyMock = mock(Survey.class);

        Question questionMock = QuestionFixtureGenerator.generateQuestionFixture();
        ReflectionTestUtils.setField(questionMock, "survey", surveyMock);

        QuestionUpdateRequestDto requestDto = new QuestionUpdateRequestDto(number, content, null);

        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(surveyMock));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(questionMock));

        when(userMock.isUserRoleSurveyee()).thenReturn(false);
        when(surveyMock.isUserSurveyCreator(userMock)).thenReturn(false);
        when(userMock.isUserRoleNotAdmin()).thenReturn(false);
        when(surveyMock.isSurveyStatusNotStarted()).thenReturn(true);

//
//        doNothing().when(questionMock).changeNumber(requestDto.getNumber());
//        doNothing().when(questionMock).changeContent(requestDto.getContent());

        when(questionRepository.save(any(Question.class))).thenReturn(questionMock);

        //when
        QuestionResponseDto responseDto = questionService.updateQuestion(userMock, surveyId, questionId, requestDto);

        //then

        assertThat(responseDto.getNumber()).isEqualTo(number);
        assertThat(responseDto.getContent()).isEqualTo(content);
        assertThat(responseDto.getType()).isEqualTo(questionMock.getType());
        verify(surveyRepository).findById(surveyId);
        verify(questionRepository).findById(questionId);
        verify(userMock).isUserRoleSurveyee();
        verify(userMock).isUserRoleNotAdmin();
        verify(surveyMock).isUserSurveyCreator(userMock);
        verify(surveyMock).isSurveyStatusNotStarted();
        verify(questionRepository).save(any(Question.class));
    }

    @Test
    void 해당설문_출제자가_질문을_삭제한다(){
        Long surveyId = 1L;
        Long questionId = 1L;

        User userMock = mock(User.class);
        Survey surveyMock = mock(Survey.class);

        Question questionMock = QuestionFixtureGenerator.generateQuestionFixture();
        ReflectionTestUtils.setField(questionMock, "survey", surveyMock);

        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(surveyMock));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(questionMock));
        when(userMock.isUserRoleSurveyee()).thenReturn(false);
        when(surveyMock.isUserSurveyCreator(userMock)).thenReturn(true);
        when(surveyMock.isSurveyStatusNotStarted()).thenReturn(true);

        doNothing().when(questionRepository).delete(questionMock);

        //when
        questionService.deleteQuestion(userMock, surveyId, questionId);

        //then
        verify(surveyRepository).findById(surveyId);
        verify(questionRepository).findById(questionId);
        verify(userMock).isUserRoleSurveyee();
        verify(surveyMock).isUserSurveyCreator(userMock);
        verify(surveyMock).isSurveyStatusNotStarted();
        verify(questionRepository).delete(questionMock);
    }
}
