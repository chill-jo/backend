package com.example.surveyapp.domain.survey.service;

import com.example.surveyapp.domain.survey.controller.dto.request.QuestionCreateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.QuestionUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.response.QuestionResponseDto;
import com.example.surveyapp.domain.survey.domain.model.entity.Question;
import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.survey.domain.model.enums.SurveyStatus;
import com.example.surveyapp.domain.survey.domain.repository.QuestionRepository;
import com.example.surveyapp.domain.survey.domain.repository.SurveyRepository;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;

    //설문 상태 확인하는거 만들어야할듯
    //해당 설문 출제자인지 확인해야함
    public void currentUserMatchesSurveyCreator(Long userId, Survey survey, String errorMessage){
        Long surveyCreatorId = survey.getUser().getId();

        if(!userId.equals(surveyCreatorId)){
            throw new RuntimeException(errorMessage);
        }
    }

    public void isSurveyNotStarted(Survey survey, String errorMessage){
        SurveyStatus currentStatus = survey.getStatus();

        if(!currentStatus.equals(SurveyStatus.NOT_STARTED)){
            throw new RuntimeException(errorMessage);
        }
    }

    public QuestionResponseDto createQuestion(Long userId, Long surveyId, QuestionCreateRequestDto requestDto){

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));

        currentUserMatchesSurveyCreator(userId, survey, "설문 출제자가 아닌 유저는 질문을 생성할 수 없습니다.");
        isSurveyNotStarted(survey, "설문이 진행 전 상태일 때만 질문을 생성할 수 있습니다.");

        Question question = new Question(
                survey,
                requestDto.getNumber(),
                requestDto.getContent(),
                requestDto.getType()
        );

        questionRepository.save(question);

        return new QuestionResponseDto(question.getId(), question.getNumber(), question.getContent(), question.getType());
    }

    public QuestionResponseDto updateQuestion(Long userId, Long surveyId, Long questionId, QuestionUpdateRequestDto requestDto){

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new RuntimeException("질문을 찾을 수 없습니다."));

        currentUserMatchesSurveyCreator(userId, survey, "설문 출제자가 아닌 유저는 질문을 수정할 수 없습니다.");
        isSurveyNotStarted(survey, "설문이 진행 전 상태일 때만 질문을 수정할 수 있습니다.");

        if(!surveyId.equals(question.getSurvey().getId())){
            throw new RuntimeException("질문이 설문에 존재하지 않습니다.");
        }

        if(requestDto.getNumber() != null){
            question.setNumber(requestDto.getNumber());
        }
        if(requestDto.getContent() != null){
            question.setContent(requestDto.getContent());
        }
        //주관식 -> 선택형 or 선택형 -> 주관식으로 바꿨을 때 선택지가 필수적으로 추가되거나 삭제되어야하는데
        //어떻게 하지?
        if(requestDto.getType() != null){
            question.setType(requestDto.getType());
        }

        questionRepository.save(question);

        return new QuestionResponseDto(question.getId(), question.getNumber(), question.getContent(), question.getType());

    }

    @Transactional
    public void deleteQuestion(Long userId, Long surveyId, Long questionId){

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new RuntimeException("질문을 찾을 수 없습니다."));

        currentUserMatchesSurveyCreator(userId, survey, "설문 출제자가 아닌 유저는 질문을 삭제할 수 없습니다.");
        isSurveyNotStarted(survey, "설문이 진행 전 상태일 때만 질문을 삭제할 수 있습니다.");

        if(!surveyId.equals(question.getSurvey().getId())){
            throw new RuntimeException("질문이 설문에 존재하지 않습니다.");
        }

        questionRepository.delete(question);

    }
}
