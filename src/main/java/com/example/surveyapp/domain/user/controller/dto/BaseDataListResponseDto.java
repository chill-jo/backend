package com.example.surveyapp.domain.user.controller.dto;

import com.example.surveyapp.domain.user.domain.model.UserBaseData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BaseDataListResponseDto {

    private final List<BaseDataResponseDto> list;

}
