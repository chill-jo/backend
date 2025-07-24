package com.example.surveyapp.domain.survey.domain.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "survey_options_answer")
@AllArgsConstructor
@NoArgsConstructor
public class SurveyOptionsAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_answer_id", nullable = false)
    private SurveyAnswer surveyAnswerId;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question questionId;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Options optionId;

    @Column(nullable = false)
    private Long number;
}
