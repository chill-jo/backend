package com.example.surveyapp.domain.survey.service;

import com.example.surveyapp.domain.survey.controller.dto.request.OptionCreateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.OptionUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.response.OptionResponseDto;
import com.example.surveyapp.domain.survey.domain.model.entity.Options;
import com.example.surveyapp.domain.survey.domain.model.entity.Question;
import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.survey.domain.model.enums.SurveyStatus;
import com.example.surveyapp.domain.survey.domain.repository.OptionsRepository;
import com.example.surveyapp.domain.survey.domain.repository.QuestionRepository;
import com.example.surveyapp.domain.survey.domain.repository.SurveyRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionsService {

    private final OptionsRepository optionsRepository;
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;

    //참여자 권한 막기(생성, 수정, 삭제, 진행중아닌 설문선택지조회)
    public void isCurrentUserSurveyee(Long userId, String message){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if(user.isUserRoleSurveyee()){
            throw new CustomException(ErrorCode.SURVEYEE_NOT_ALLOWED, message);
        }
    }

    //해당 설문 출제자가 아니거나 관리자가 아닐 시 예외
    public void currentUserMatchesSurveyCreator(Long userId, Survey survey, String errorMessage){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if(!survey.isUserSurveyCreator(user) && user.isUserRoleNotAdmin()){
            throw new CustomException(ErrorCode.NOT_SURVEY_CREATOR, errorMessage);
        }
    }

    //설문 상태가 진행 전이 아닐 때 생성, 수정, 삭제 시 예외
    public void isSurveyNotStarted(Survey survey, String errorMessage){
        if(!survey.isSurveyStatusNotStarted()){
            throw new CustomException(ErrorCode.SURVEY_NOT_STARTED, errorMessage);
        }
    }

    //설문에 포함된 질문이 아닐 때 예외
    public void isQuestionFromSurvey(Survey survey, Question question){
        if(!question.isFromSurvey(survey)){
            throw new CustomException(ErrorCode.QUESTION_NOT_FROM_SURVEY);
        }
    }

    //질문에 포함된 선택지가 아닐 때 예외
    public void isOptionFromQuestion(Question question, Options option){
        if(!option.isFromQuestion(question)){
            throw new CustomException(ErrorCode.OPTIONS_NOT_FROM_SURVEY);
        }
    }

    public OptionResponseDto createOption(Long userId, Long surveyId, Long questionId, OptionCreateRequestDto requestDto){

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        if(survey.isDeleted()){ throw new CustomException(ErrorCode.SURVEY_ALREADY_DELETED); }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        isCurrentUserSurveyee(userId, "참여자 권한으로는 선택지를 생성할 수 없습니다.");
        currentUserMatchesSurveyCreator(userId, survey, "설문 출제자와 관리자만 선택지를 생성할 수 있습니다.");
        isSurveyNotStarted(survey, "설문이 진행 전 상태일 때만 선택지를 생성할 수 있습니다.");
        isQuestionFromSurvey(survey, question);

        Options option = new Options(requestDto.getNumber(), requestDto.getContent());

        optionsRepository.save(option);

        return new OptionResponseDto(option.getId(), option.getNumber(), option.getContent());
    }

    //(inprogress일때는 모두, 다른 상태에는 관리자랑 해당 설문 출제자만 조회가능)
    //in progress이고 유저가 참여자권한인 경우 이미 참여했는지 확인해야함. - 응답 도메인 생성 후 수정
    public List<OptionResponseDto> getOptions(Long userId, Long surveyId, Long questionId){

        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        if(survey.isDeleted()){ throw new CustomException(ErrorCode.SURVEY_ALREADY_DELETED); }

        questionRepository.findById(questionId).orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        //진행 중 설문이 아닐 때는 참여자, 해당 설문 출제자가 아닌 출제자는 조회 불가.
        if(!survey.isSurveyStatusInProgress()){
            isCurrentUserSurveyee(userId, "진행 중 상태가 아닌 설문의 선택지는 설문 참여자 권한으로 조회할 수 없습니다.");
            currentUserMatchesSurveyCreator(userId, survey, "설문 출제자와 관리자만 진행 중이 아닌 설문의 선택지를 조회할 수 있습니다.");
        }

        List<Options> optionsList = optionsRepository.findAllByQuestionId(questionId);

        List<OptionResponseDto> dtoList = optionsList.stream()
                .map(option -> new OptionResponseDto(option.getId(), option.getNumber(), option.getContent()))
                .collect(Collectors.toList());

        return dtoList;
    }

    public OptionResponseDto updateOption(Long userId, Long surveyId, Long questionId, Long optionId, OptionUpdateRequestDto requestDto){

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        if(survey.isDeleted()){ throw new CustomException(ErrorCode.SURVEY_ALREADY_DELETED); }

        Question question = questionRepository.findById(questionId).orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        Options option = optionsRepository.findById(optionId).orElseThrow(() -> new CustomException(ErrorCode.OPTION_NOT_FOUND));

        isCurrentUserSurveyee(userId, "참여자 권한으로는 선택지를 수정할 수 없습니다.");
        currentUserMatchesSurveyCreator(userId, survey, "설문 출제자와 관리자만 선택지를 수정할 수 있습니다.");
        isSurveyNotStarted(survey, "설문이 진행 전 상태일 때만 선택지를 수정할 수 있습니다.");
        isQuestionFromSurvey(survey, question);
        isOptionFromQuestion(question, option);

        if(requestDto.getNumber() != null){
            option.changeNumber(requestDto.getNumber());
        }
        if(requestDto.getContent() != null){
            option.changeContent(requestDto.getContent());
        }

        optionsRepository.save(option);

        return new OptionResponseDto(option.getId(), option.getNumber(), option.getContent());
    }

    @Transactional
    public void deleteOption(Long userId, Long surveyId, Long questionId, Long optionId){

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        if(survey.isDeleted()){ throw new CustomException(ErrorCode.SURVEY_ALREADY_DELETED); }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        Options option = optionsRepository.findById(optionId)
                .orElseThrow(() -> new CustomException(ErrorCode.OPTION_NOT_FOUND));

        isCurrentUserSurveyee(userId, "참여자 권한으로는 선택지를 삭제할 수 없습니다.");
        currentUserMatchesSurveyCreator(userId, survey, "설문 출제자와 관리자만 선택지를 삭제할 수 있습니다.");
        isSurveyNotStarted(survey, "설문이 진행 전 상태일 때만 선택지를 삭제할 수 있습니다.");
        isQuestionFromSurvey(survey, question);
        isOptionFromQuestion(question, option);

        optionsRepository.delete(option);

    }
}
