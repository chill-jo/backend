package com.example.surveyapp.domain.survey.domain.repository;

import com.example.surveyapp.domain.survey.domain.model.entity.Question;
import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q WHERE q.survey.id = :surveyId")
    Page<Question> findAllBySurveyId(@Param("surveyId") Long surveyId, Pageable pageable);

    @Query("SELECT q FROM Question q WHERE q.survey.id = :surveyId ORDER BY q.number ASC")
    List<Question> findAllBySurveyIdOrderByNumberASC(@Param("surveyId") Long surveyId);

    List<Question> findAllBySurvey(Survey survey);

    void deleteAllBySurvey(Survey survey);
}