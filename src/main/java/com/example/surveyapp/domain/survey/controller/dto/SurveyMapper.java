package com.example.surveyapp.domain.survey.controller.dto;

import com.example.surveyapp.domain.survey.controller.dto.request.SurveyCreateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.response.SurveyResponseDto;
import com.example.surveyapp.domain.survey.domain.model.entity.SurveyStatus;
import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.user.domain.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SurveyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "status", expression = "java(SurveyStatus.NOT_STARTED)")
    @Mapping(target = "deleted", constant = "false")
    Survey createSurveyEntity(SurveyCreateRequestDto dto, User user);

    SurveyResponseDto toResponseDto(Survey survey);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSurvey(SurveyUpdateRequestDto dto, @MappingTarget Survey survey);



}
