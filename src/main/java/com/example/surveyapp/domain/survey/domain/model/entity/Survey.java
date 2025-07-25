package com.example.surveyapp.domain.survey.domain.model.entity;

import com.example.surveyapp.domain.survey.domain.model.enums.SurveyStatus;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.global.config.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "survey")
public class Survey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private Long maxSurveyee;

    @Column(nullable = false)
    private Long pointPerPerson;

    @Column(nullable = false)
    private Long totalPoint;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false)
    private Long expectedTime;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SurveyStatus status;

    @Column(nullable = false)
    private boolean deleted;

    public void deleteSurvey(){ this.deleted = true; }
    public void changeSurveyStatus(SurveyStatus newStatus) {this.status = newStatus;}
    public void changeTotalPoint(Long totalPoint){ this.totalPoint = totalPoint; }
    public boolean isSurveyStatusNotStarted(){
        return this.status.equals(SurveyStatus.NOT_STARTED);
    }
    public boolean isSurveyStatusInProgress(){
        return this.status.equals(SurveyStatus.IN_PROGRESS);
    }
    public boolean isUserSurveyCreator(User user){
        return this.user.equals(user);
    }

    public Survey(User user, String title, String description, Long maxSurveyee, Long pointPerPerson,
                  LocalDateTime deadline, Long expectedTime) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.maxSurveyee = maxSurveyee;
        this.pointPerPerson = pointPerPerson;
        this.totalPoint = maxSurveyee * pointPerPerson;
        this.deadline = deadline;
        this.expectedTime = expectedTime;
        this.status = SurveyStatus.NOT_STARTED;
        this.deleted = false;
    }

    public void update(String title, String description, Long maxSurveyee, Long pointPerPerson,
                       LocalDateTime deadline, Long expectedTime) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (maxSurveyee != null) this.maxSurveyee = maxSurveyee;
        if (pointPerPerson != null) this.pointPerPerson = pointPerPerson;
        if (deadline != null) this.deadline = deadline;
        if (expectedTime != null) this.expectedTime = expectedTime;
    }

    public static Survey of(User user, String title, String description, Long maxSurveyee,
                            Long pointPerPerson, LocalDateTime deadline, Long expectedTime) {
        return new Survey(user, title, description, maxSurveyee, pointPerPerson, deadline, expectedTime);
    }



}
