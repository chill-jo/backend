package com.example.surveyapp.domain.survey.domain.repository;

import com.example.surveyapp.domain.survey.domain.model.entity.Question;
import com.example.surveyapp.domain.survey.domain.model.entity.SurveyOptionsAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SurveyOptionsAnswerRepository extends JpaRepository<SurveyOptionsAnswer, Long> {

    @Query("""
            SELECT COUNT(o)
            FROM SurveyOptionsAnswer AS o
            LEFT JOIN FETCH SurveyAnswer AS a
                    ON o.surveyAnswerId = a
            WHERE o.questionId = :questionId
                    AND o.number = :number
                    AND a.createdAt >= :startDate
                    AND a.createdAt <= :endDate
            """)
    Long countByQuestionIdAndNumberAndStartDateAndEndDate(
            Question questionId,
            Long number,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
