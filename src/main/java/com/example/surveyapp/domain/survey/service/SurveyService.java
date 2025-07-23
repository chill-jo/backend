package com.example.surveyapp.domain.survey.service;

import com.example.surveyapp.domain.survey.controller.dto.SurveyMapper;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyCreateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyStatusUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.response.SurveyResponseDto;
import com.example.surveyapp.domain.survey.controller.dto.response.SurveyStatusResponseDto;
import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.survey.domain.model.entity.SurveyStatus;
import com.example.surveyapp.domain.survey.domain.repository.SurveyRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyMapper surveyMapper;

    public SurveyResponseDto createSurvey(SurveyCreateRequestDto requestDto, User user){

        Survey survey = surveyMapper.createSurveyEntity(requestDto, user);

        Survey saved = surveyRepository.save(survey);

        return surveyMapper.toResponseDto(saved);
    }

    //삭제되지 않은 설문만
//    public SurveyResponseDto getSurveys(){
//
//    }
    public SurveyResponseDto updateSurvey(Long surveyId, SurveyUpdateRequestDto requestDto){

        Survey survey = surveyRepository.findByIdAndDeletedFalse(surveyId).orElseThrow(
                () -> new RuntimeException("삭제되었거나 존재하지 않는 설문입니다.")
        );

        if(survey.getStatus() != SurveyStatus.NOT_STARTED){
            throw new RuntimeException("설문 상태가 진행 전일 때만 상세정보 수정이 가능합니다.");
        }

        surveyMapper.updateSurvey(requestDto, survey);

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
            throw new RuntimeException("현재 설문 상태와 다른 상태로 변경해야 합니다.");
        }
        if(newStatus == SurveyStatus.NOT_STARTED){
            throw new RuntimeException("설문은 진행 전 상태로 변경할 수 없습니다.");
        }
        else if(newStatus == SurveyStatus.IN_PROGRESS){
            if(currentStatus == SurveyStatus.DONE){
                throw new RuntimeException("마감된 설문은 진행 중 상태로 변경할 수 없습니다.");
            }
        }
        else if(newStatus == SurveyStatus.PAUSED){
            if(currentStatus != SurveyStatus.IN_PROGRESS){
                throw new RuntimeException("설문이 진행 중일 때만 일시정지 상태로 변경할 수 있습니다.");
            }
        }
        else if(newStatus == SurveyStatus.DONE){
            if(currentStatus == SurveyStatus.NOT_STARTED){
                throw new RuntimeException("진행 전 설문은 마감 상태로 변경할 수 없습니다.");
            }
        }

        survey.updateSurveyStatus(newStatus);
        surveyRepository.save(survey);

        return new SurveyStatusResponseDto(survey.getStatus());
    }

    public void deleteSurvey(Long surveyId){

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 설문입니다.")
        );

        if(survey.isDeleted()){
            throw new RuntimeException("이미 삭제된 설문입니다.");
        }

        if(survey.getStatus() == SurveyStatus.IN_PROGRESS){
            throw new RuntimeException("진행 중 설문은 삭제할 수 없습니다.");
        }

        survey.deleteSurvey();
    }

}
