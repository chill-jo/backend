package com.example.surveyapp.domain.survey.service;

import com.example.surveyapp.domain.survey.controller.dto.SurveyMapper;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyAnswerRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyCreateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyStatusUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.response.*;
import com.example.surveyapp.domain.survey.domain.model.entity.*;
import com.example.surveyapp.domain.survey.domain.model.enums.SurveyStatus;
import com.example.surveyapp.domain.survey.domain.repository.*;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.surveyapp.domain.survey.domain.model.enums.QuestionType.SINGLE_CHOICE;
import static com.example.surveyapp.domain.survey.domain.model.enums.QuestionType.SUBJECTIVE;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyMapper surveyMapper;
    private final QuestionRepository questionRepository;
    private final OptionsRepository optionsRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final UserRepository userRepository;
    private final SurveyOptionsAnswerRepository surveyOptionsAnswerRepository;
    private final SurveyTextAnswerRepository surveyTextAnswerRepository;

    public SurveyResponseDto createSurvey(SurveyCreateRequestDto requestDto, User user){

        Survey survey = surveyMapper.createSurveyEntity(requestDto, user);

        Survey saved = surveyRepository.save(survey);

        return surveyMapper.toResponseDto(saved);
    }

    //삭제되지 않은 설문만
    @Transactional(readOnly = true)
    public PageSurveyResponseDto<SurveyResponseDto> getSurveys(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Survey> surveyPage = surveyRepository.findAllSurveyPaged(pageable);

        Page<SurveyResponseDto> surveyResponseDtoPage = surveyPage.map(surveyMapper::toResponseDto);

        return new PageSurveyResponseDto<>(surveyResponseDtoPage);
    }


    public SurveyResponseDto updateSurvey(Long surveyId, SurveyUpdateRequestDto requestDto){

        Survey survey = surveyRepository.findByIdAndDeletedFalse(surveyId).orElseThrow(
                () -> new CustomException(ErrorCode.SURVEY_NOT_FOUND,"삭제되었거나 존재하지 않는 설문입니다.")
        );

        if(!survey.isSurveyStatusNotStarted()){
            throw new CustomException(ErrorCode.SURVEY_CANNOT_BE_MODIFIED,"설문 상태가 진행 전일 때만 상세정보 수정이 가능합니다.");
        }

        surveyMapper.updateSurvey(requestDto, survey);

        Long totalPoint = survey.getPointPerPerson() * survey.getMaxSurveyee();
        survey.changeTotalPoint(totalPoint);

        surveyRepository.save(survey);

        return surveyMapper.toResponseDto(survey);
    }

    //설문 상태 변경(NOT_STARTED -> IN_PROGRESS, IN_PROGRESS -> PAUSED, PAUSED -> IN_PROGRESS, IN_PROGRESS -> DONE)
    public SurveyStatusResponseDto updateSurveyStatus(Long surveyId, SurveyStatusUpdateRequestDto requestDto){

        Survey survey = surveyRepository.findByIdAndDeletedFalse(surveyId).orElseThrow(
                () -> new CustomException(ErrorCode.UNEXPECTED_ERROR,"삭제되었거나 존재하지 않는 설문입니다.")
        );

        SurveyStatus currentStatus = survey.getStatus();
        SurveyStatus newStatus = requestDto.getStatus();

        if(currentStatus.equals(newStatus)){
            throw new CustomException(ErrorCode.INVALID_SURVEY_STATUS_TRANSITION, "현재 설문 상태와 다른 상태로 변경해야 합니다.");
        }
        if(newStatus.equals(SurveyStatus.NOT_STARTED)){
            throw new CustomException(ErrorCode.INVALID_SURVEY_STATUS_TRANSITION, "설문은 진행 전 상태로 변경할 수 없습니다.");
        }
        if(newStatus.equals(SurveyStatus.IN_PROGRESS) && currentStatus.equals(SurveyStatus.DONE)){
                throw new CustomException(ErrorCode.INVALID_SURVEY_STATUS_TRANSITION, "마감된 설문은 진행 중 상태로 변경할 수 없습니다.");
        }
        if(newStatus.equals(SurveyStatus.PAUSED) && !currentStatus.equals(SurveyStatus.IN_PROGRESS)){
                throw new CustomException(ErrorCode.INVALID_SURVEY_STATUS_TRANSITION, "설문이 진행 중일 때만 일시정지 상태로 변경할 수 있습니다.");
        }
        if(newStatus.equals(SurveyStatus.DONE) && currentStatus.equals(SurveyStatus.NOT_STARTED)){
                throw new CustomException(ErrorCode.INVALID_SURVEY_STATUS_TRANSITION, "진행 전 설문은 마감 상태로 변경할 수 없습니다.");
        }

        survey.changeSurveyStatus(newStatus);
        surveyRepository.save(survey);

        return new SurveyStatusResponseDto(survey.getStatus());
    }

    @Transactional
    public void deleteSurvey(Long surveyId){

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(ErrorCode.SURVEY_NOT_FOUND, "존재하지 않는 설문입니다.")
        );

        if(survey.isDeleted()){
            throw new CustomException(ErrorCode.SURVEY_ALREADY_DELETED, "이미 삭제된 설문입니다.");
        }

        if(survey.isSurveyStatusInProgress()){
            throw new CustomException(ErrorCode.SURVEY_CANNOT_BE_DELETED, "진행 중 설문은 삭제할 수 없습니다.");
        }

        survey.deleteSurvey();
    }


    // 참여자 API
    // 삭제 되지 않은 설문만 설문 상세 조회
    @Transactional
    public SurveyResponseDto getSurvey(Long surveyId){

        Survey survey = surveyRepository.findByIdAndDeletedFalse(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));

        return surveyMapper.toResponseDto(survey);
    }

    // 설문 시작
    // survey_answer 테이블 생기면 기참여자 재참여 못하게 막는 로직 추가해야함
    @Transactional(readOnly = true)
    public SurveyQuestionDto startSurvey(Long surveyId){

        Survey survey = surveyRepository.findByIdAndDeletedFalse(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));

        SurveyQuestionDto surveyQuestionDto = new SurveyQuestionDto(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                survey.getMaxSurveyee(),
                survey.getPointPerPerson(),
                survey.getTotalPoint(),
                survey.getDeadline(),
                survey.getExpectedTime(),
                new ArrayList<>()
        );

        List<Question> questions = questionRepository.findAllBySurveyIdOrderByNumberASC(surveyId);

        questions.forEach(question -> {
            List<OptionResponseDto> options = optionsRepository.findAllByQuestionIdOrderByNumberAsc(question.getId());
            QuestionOptionsDto questionOptionsDto = new QuestionOptionsDto(
                    question.getId(),
                    question.getNumber(),
                    question.getContent(),
                    question.getType(),
                    options
            );

            surveyQuestionDto.addQuestion(questionOptionsDto);
        });


        return surveyQuestionDto;
    }

    @Transactional
//    public void saveSurveyAnswer(Long surveyId, SurveyAnswerRequestDto requestDto, Long userId){
    public void saveSurveyAnswer(Long surveyId, SurveyAnswerRequestDto requestDto){

        Survey survey = surveyRepository.findByIdAndDeletedFalse(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));

        Long userId = 1L;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        SurveyAnswer surveyAnswer = surveyAnswerRepository.save(new SurveyAnswer(survey, user));

        requestDto.getAnswers().forEach(questionAnswer -> {
            Question question = questionRepository.findById(questionAnswer.getNumber()).orElseThrow(
                    () -> new CustomException(ErrorCode.QUESTION_NOT_FOUND)
            );

            if(question.getType().equals(SUBJECTIVE)) {
                surveyTextAnswerRepository.save(new SurveyTextAnswer(surveyAnswer, question, (String)questionAnswer.getAnswer()));
            } else if(question.getType().equals(SINGLE_CHOICE)) {
                Number answer = (Number)questionAnswer.getAnswer();
                surveyOptionsAnswerRepository.save(new SurveyOptionsAnswer(surveyAnswer, question, answer.longValue()));
            } else {
                String str = (String) questionAnswer.getAnswer();
                String[] split = str.split(",");
                for (String s : split) {
                    try {
                        Long number = Long.parseLong(s);
                        surveyOptionsAnswerRepository.save(new SurveyOptionsAnswer(surveyAnswer, question, number));
                    } catch (NumberFormatException e) {
                        throw new CustomException(ErrorCode.VALIDATION_ERROR);
                    }
                }
            }
        });
    }

    @Transactional(readOnly = true)
    public SurveyeeSurveyListDto getSurveyeeSurveyList() {

        // User 추가시 userId 수정
        Long userId = 1L;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        List<SurveyAnswer> surveyAnswerList = surveyAnswerRepository.findAllByUserIdOrderByCreatedAtDesc(user);

        SurveyeeSurveyListDto surveyListDto = new SurveyeeSurveyListDto();

        surveyAnswerList.forEach(surveyAnswer -> {
            SurveyeeSurveyDto surveyeeSurveyDto = SurveyeeSurveyDto.builder()
                                                    .surveyId(surveyAnswer.getSurveyId().getId())
                                                    .title(surveyAnswer.getSurveyId().getTitle())
                                                    .date(surveyAnswer.getCreatedAt())
                                                    .build();

            surveyListDto.addSurveyeeSurveyDto(surveyeeSurveyDto);
        });

        return surveyListDto;
    }

}
