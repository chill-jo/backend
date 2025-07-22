package com.example.surveyapp.domain.survey.dto.request;

import com.example.surveyapp.domain.survey.entity.SurveyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SurveyStatusUpdateRequestDto {

    @NotBlank
    private SurveyStatus status;

    public SurveyStatusUpdateRequestDto(SurveyStatus status){
        this.status = status;
    }
}
