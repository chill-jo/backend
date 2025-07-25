package com.example.surveyapp.domain.survey.domain.model.entity;

import com.example.surveyapp.domain.user.domain.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "survey_answer")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SurveyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey surveyId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public SurveyAnswer(Survey surveyId, User userId) {
        this.surveyId = surveyId;
        this.userId = userId;
    }
}
