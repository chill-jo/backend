package com.example.surveyapp.domain.survey.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class QuestionCreateRequest {

    @NotNull
    private final Long number;

    @NotBlank
    @Length(min = 5, max = 255, message = "질문 내용은 5~255자 사이여야 합니다.")
    private final String content;
}
