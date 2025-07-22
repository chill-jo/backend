package com.example.surveyapp.domain.survey.entity;

import com.example.surveyapp.domain.survey.dto.request.SurveyUpdateRequestDto;
import com.example.surveyapp.global.response.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "survey")
public class Survey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    User 도메인 추가 후 사용
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

    private String title;

    private String description;

    private Long maxSurveyee;

    private Long pointPerPerson;

    private Long totalPoint;

    private LocalDateTime deadline;

    private Long expectedTime;

    private SurveyStatus status;

    private boolean isDeleted;

    public void updateSurvey(SurveyUpdateRequestDto requestDto){

    }


}
