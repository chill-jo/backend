package com.example.surveyapp.domain.survey.domain.model.enums;

import java.util.Arrays;

public enum QuestionType {
    SINGLE_CHOICE("객관식"),
    MULTIPLE_CHOICE("중복 선택"),
    SUBJECTIVE("주관식");

    private final String questionType;

    QuestionType(String questionType){
        this.questionType = questionType;
    }

    public static QuestionType of(String input){
        return Arrays.stream(QuestionType.values())
                .filter(s -> s.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("질문 종류 오류"));
    }


    public boolean isSubjective() {
        return this == SUBJECTIVE;
    }

    public boolean isSingleChoice() {
        return this == SINGLE_CHOICE;
    }

    public boolean isMultiChoice() {
        return this == MULTIPLE_CHOICE;
    }
}
