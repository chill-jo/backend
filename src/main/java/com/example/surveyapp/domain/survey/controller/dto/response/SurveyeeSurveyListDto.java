package com.example.surveyapp.domain.survey.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SurveyeeSurveyListDto {

    private List<SurveyeeSurveyDto> survey = new ArrayList<>();

    public void addSurveyeeSurveyDto(SurveyeeSurveyDto surveyeeSurveyDto){
        this.survey.add(surveyeeSurveyDto);
    }
}
