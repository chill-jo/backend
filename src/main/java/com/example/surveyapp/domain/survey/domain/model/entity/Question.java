package com.example.surveyapp.domain.survey.domain.model.entity;

import com.example.surveyapp.domain.survey.domain.model.enums.QuestionType;
import com.example.surveyapp.global.config.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "question")
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Column(nullable = false)
    private Long number;

    @Column(nullable = false, length = 255)
    private String content;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private QuestionType type;

    public Question(Survey survey, Long number, String content, QuestionType type){
        this.survey = survey;
        this.number = number;
        this.content = content;
        this.type = type;
    }
    public void changeNumber(Long number){
        this.number = number;
    }
    public void changeContent(String content){
        this.content = content;
    }
    public void changeQuestionType(QuestionType type){
        this.type = type;
    }
    public boolean isFromSurvey(Survey survey){
        return this.survey.equals(survey);
    }

    public static Question of(Survey survey, Long number, String content, QuestionType type) {
        return new Question(survey, number,content, type);
    }
}
