package com.example.surveyapp.domain.survey.service;

import com.example.surveyapp.domain.survey.controller.dto.SurveyMapper;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyCreateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyStatusUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.response.*;
import com.example.surveyapp.domain.survey.domain.model.entity.Question;
import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.survey.domain.model.enums.SurveyStatus;
import com.example.surveyapp.domain.survey.domain.repository.OptionsRepository;
import com.example.surveyapp.domain.survey.domain.repository.QuestionRepository;
import com.example.surveyapp.domain.survey.domain.repository.SurveyRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyMapper surveyMapper;
    private final QuestionRepository questionRepository;
    private final OptionsRepository optionsRepository;

    public SurveyResponseDto createSurvey(SurveyCreateRequestDto requestDto, User user){

        Survey survey = surveyMapper.createSurveyEntity(requestDto, user);

        Survey saved = surveyRepository.save(survey);

        return surveyMapper.toResponseDto(saved);
    }

    //삭제되지 않은 설문만
    @Transactional(readOnly = true)
    public PageSurveyResponseDto<SurveyResponseDto> getSurveys(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Survey> surveyPage = surveyRepository.findAllSurveyPaged(pageable);

        Page<SurveyResponseDto> surveyResponseDtoPage = surveyPage.map(surveyMapper::toResponseDto);

        return new PageSurveyResponseDto<>(surveyResponseDtoPage);
    }


    public SurveyResponseDto updateSurvey(Long surveyId, SurveyUpdateRequestDto requestDto){

        Survey survey = surveyRepository.findByIdAndDeletedFalse(surveyId).orElseThrow(
                () -> new CustomException(ErrorCode.SURVEY_NOT_FOUND,"삭제되었거나 존재하지 않는 설문입니다.")
        );

        if(survey.getStatus() != SurveyStatus.NOT_STARTED){
            throw new CustomException(ErrorCode.SURVEY_CANNOT_BE_MODIFIED,"설문 상태가 진행 전일 때만 상세정보 수정이 가능합니다.");
        }

        surveyMapper.updateSurvey(requestDto, survey);

        Long totalPoint = survey.getPointPerPerson() * survey.getMaxSurveyee();
        survey.setTotalPoint(totalPoint);

        surveyRepository.save(survey);

        return surveyMapper.toResponseDto(survey);
    }

    //설문 상태 변경(NOT_STARTED -> IN_PROGRESS, IN_PROGRESS -> PAUSED, PAUSED -> IN_PROGRESS, IN_PROGRESS -> DONE)
    public SurveyStatusResponseDto updateSurveyStatus(Long surveyId, SurveyStatusUpdateRequestDto requestDto){

        Survey survey = surveyRepository.findByIdAndDeletedFalse(surveyId).orElseThrow(
                () -> new CustomException(ErrorCode.UNEXPECTED_ERROR,"삭제되었거나 존재하지 않는 설문입니다.")
        );

        SurveyStatus currentStatus = survey.getStatus();
        SurveyStatus newStatus = requestDto.getStatus();

        if(currentStatus == newStatus){
            throw new CustomException(ErrorCode.INVALID_SURVEY_STATUS_TRANSITION, "현재 설문 상태와 다른 상태로 변경해야 합니다.");
        }
        if(newStatus == SurveyStatus.NOT_STARTED){
            throw new CustomException(ErrorCode.INVALID_SURVEY_STATUS_TRANSITION, "설문은 진행 전 상태로 변경할 수 없습니다.");
        }
        else if(newStatus == SurveyStatus.IN_PROGRESS){
            if(currentStatus == SurveyStatus.DONE){
                throw new CustomException(ErrorCode.INVALID_SURVEY_STATUS_TRANSITION, "마감된 설문은 진행 중 상태로 변경할 수 없습니다.");
            }
        }
        else if(newStatus == SurveyStatus.PAUSED){
            if(currentStatus != SurveyStatus.IN_PROGRESS){
                throw new CustomException(ErrorCode.INVALID_SURVEY_STATUS_TRANSITION, "설문이 진행 중일 때만 일시정지 상태로 변경할 수 있습니다.");
            }
        }
        else if(newStatus == SurveyStatus.DONE){
            if(currentStatus == SurveyStatus.NOT_STARTED){
                throw new CustomException(ErrorCode.INVALID_SURVEY_STATUS_TRANSITION, "진행 전 설문은 마감 상태로 변경할 수 없습니다.");
            }
        }

        survey.updateSurveyStatus(newStatus);
        surveyRepository.save(survey);

        return new SurveyStatusResponseDto(survey.getStatus());
    }

    @Transactional
    public void deleteSurvey(Long surveyId){

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(ErrorCode.SURVEY_NOT_FOUND, "존재하지 않는 설문입니다.")
        );

        if(survey.isDeleted()){
            throw new CustomException(ErrorCode.SURVEY_ALREADY_DELETED, "이미 삭제된 설문입니다.");
        }

        if(survey.getStatus() == SurveyStatus.IN_PROGRESS){
            throw new CustomException(ErrorCode.SURVEY_CANNOT_BE_DELETED, "진행 중 설문은 삭제할 수 없습니다.");
        }

        survey.deleteSurvey();
    }


    // 참여자 API
    // 삭제 되지 않은 설문만 설문 상세 조회
    @Transactional
    public SurveyResponseDto getSurvey(Long surveyId){

        Survey survey = surveyRepository.findByIdAndDeletedFalse(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));

        return surveyMapper.toResponseDto(survey);
    }

    // 설문 시작
    // survey_answer 테이블 생기면 기참여자 재참여 못하게 막는 로직 추가해야함
    @Transactional(readOnly = true)
    public SurveyQuestionDto startSurvey(Long surveyId){

        Survey survey = surveyRepository.findByIdAndDeletedFalse(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));

        SurveyQuestionDto surveyQuestionDto = new SurveyQuestionDto(
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

        List<Question> questions = questionRepository.findAllBySurveyIdOrderByNumberASC(surveyId);

        questions.forEach(question -> {
            List<OptionResponseDto> options = optionsRepository.findAllByQuestionIdOrderByNumberAsc(question.getId());
            QuestionOptionsDto questionOptionsDto = new QuestionOptionsDto(
                    question.getId(),
                    question.getNumber(),
                    question.getContent(),
                    question.getType(),
                    options
            );

            surveyQuestionDto.addQuestion(questionOptionsDto);
        });


        return surveyQuestionDto;
    }

}
