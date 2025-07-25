package com.example.surveyapp.domain.admin.service;

import com.example.surveyapp.domain.admin.controller.dto.StatsDto;
import com.example.surveyapp.domain.admin.controller.dto.SurveyeeStatsDto;
import com.example.surveyapp.domain.admin.controller.dto.UserDto;
import com.example.surveyapp.domain.survey.domain.model.entity.Options;
import com.example.surveyapp.domain.survey.domain.model.entity.Question;
import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.survey.domain.model.entity.SurveyOptionsAnswer;
import com.example.surveyapp.domain.survey.domain.repository.OptionsRepository;
import com.example.surveyapp.domain.survey.domain.repository.QuestionRepository;
import com.example.surveyapp.domain.survey.domain.repository.SurveyOptionsAnswerRepository;
import com.example.surveyapp.domain.survey.domain.repository.SurveyRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final SurveyOptionsAnswerRepository surveyOptionsAnswerRepository;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final OptionsRepository optionsRepository;

    @Transactional(readOnly = true)
    public Page<UserDto> getUserList(String search, Pageable pageable) {
        return userRepository.findAllBySearch(search, pageable);
    }

    @Transactional(readOnly = true)
    public UserDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return UserDto.from(user);
    }

    @Transactional(readOnly = true)
    public List<SurveyeeStatsDto> getSurveyeeStats(LocalDateTime startDateLocal, LocalDateTime endDateLocal) {
        List<SurveyeeStatsDto> surveyeeStatsDtoList = new ArrayList<>();

        Long surveyId = 1L;
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));

        List<Question> questionList = questionRepository.findAllBySurveyIdOrderByNumberASC(survey.getId());

        questionList.forEach(question -> {
            SurveyeeStatsDto surveyeeStatsDto = new SurveyeeStatsDto(question.getContent());

            List<Options> options = optionsRepository.findAllByQuestionId(question.getId());

            options.forEach(option -> {
                Long count = surveyOptionsAnswerRepository.countByQuestionIdAndNumberAndStartDateAndEndDate(
                        question, option.getNumber(), startDateLocal, endDateLocal
                );

                StatsDto statsDto = new StatsDto(option.getContent(), count);

                surveyeeStatsDto.addOptions(statsDto);
            });

            surveyeeStatsDtoList.add(surveyeeStatsDto);

        });

        return surveyeeStatsDtoList;
    }

}
