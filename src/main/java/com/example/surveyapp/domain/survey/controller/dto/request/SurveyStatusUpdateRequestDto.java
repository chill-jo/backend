package com.example.surveyapp.domain.survey.controller.dto.request;

import com.example.surveyapp.domain.survey.domain.model.entity.SurveyStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SurveyStatusUpdateRequestDto {

    @NotBlank
    private SurveyStatus status;

    public SurveyStatusUpdateRequestDto(SurveyStatus status){
        this.status = status;
    }
}
