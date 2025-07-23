package com.example.surveyapp.domain.survey.controller.dto.request;

import com.example.surveyapp.domain.survey.domain.model.enums.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class QuestionCreateRequestDto {

    @NotNull(message = "질문 번호는 필수입니다.")
    private Long number;

    @NotBlank(message = "질문 내용은 필수입니다.")
    @Length(min = 5, max = 255, message = "질문 내용은 5~255자 사이여야 합니다.")
    private String content;

    @NotNull(message = "질문 유형은 필수입니다.")
    private QuestionType type;

}
