package com.example.surveyapp.domain.admin.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyeeStatsDto {

    private String question;

    private List<StatsDto> options = new ArrayList<>();

    public SurveyeeStatsDto(String question) {
        this.question = question;
    }

    public void addOptions(StatsDto statsDto) {
        this.options.add(statsDto);
    }

}
