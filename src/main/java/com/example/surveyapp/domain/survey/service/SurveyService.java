package com.example.surveyapp.domain.survey.service;

import com.example.surveyapp.domain.point.service.PointService;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.survey.controller.dto.SurveyMapper;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyAnswerRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyCreateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyStatusUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.response.*;
import com.example.surveyapp.domain.survey.domain.model.entity.Question;
import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.survey.domain.model.entity.SurveyAnswer;
import com.example.surveyapp.domain.survey.domain.model.enums.SurveyStatus;
import com.example.surveyapp.domain.survey.domain.repository.*;
import com.example.surveyapp.domain.survey.facade.UserFacade;
import com.example.surveyapp.domain.survey.domain.repository.OptionsRepository;
import com.example.surveyapp.domain.survey.domain.repository.QuestionRepository;
import com.example.surveyapp.domain.survey.domain.repository.SurveyAnswerRepository;
import com.example.surveyapp.domain.survey.domain.repository.SurveyRepository;
import com.example.surveyapp.domain.survey.service.strategy.SurveyQuestionStrategy;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyMapper surveyMapper;
    private final QuestionRepository questionRepository;
    private final OptionsRepository optionsRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final UserFacade userFacade;
    private final SurveyOptionsAnswerRepository surveyOptionsAnswerRepository;
    private final SurveyTextAnswerRepository surveyTextAnswerRepository;
    private final PointService pointService;
    private final List<SurveyQuestionStrategy> surveyQuestionStrategies;

    @Transactional
    public SurveyResponseDto createSurvey(Long userId, SurveyCreateRequestDto requestDto) {

        User user = userFacade.findUser(userId);


        Survey survey = surveyMapper.createSurveyEntity(requestDto, user);

        Survey saved = surveyRepository.save(survey);

        if(user.isUserRoleSurveyor()){
            pointService.surveyorRedeem(userId, saved.getTotalPoint(), saved.getId());
        }

        return surveyMapper.toResponseDto(saved);
    }

    //삭제되지 않은 설문만
    @Transactional(readOnly = true)
    public PageSurveyResponseDto<SurveyResponseDto> getSurveys(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Survey> surveyPage = surveyRepository.findAllSurveyPaged(pageable);

        Page<SurveyResponseDto> surveyResponseDtoPage = surveyPage.map(surveyMapper::toResponseDto);

        return new PageSurveyResponseDto<>(surveyResponseDtoPage);
    }

    @Transactional
    public SurveyResponseDto updateSurvey(Long userId, Long surveyId, SurveyUpdateRequestDto requestDto) {

        User user = userFacade.findUser(userId);
        Survey survey = surveyRepository.findByIdAndIsDeletedFalse(surveyId).orElseThrow(
                () -> new CustomException(ErrorCode.SURVEY_NOT_FOUND)
        );

        currentUserMatchesSurveyCreatorOrAdmin(user, survey);
        isSurveyNotStarted(survey);

        surveyMapper.updateSurvey(requestDto, survey);

        return surveyMapper.toResponseDto(survey);
    }

    @Transactional
    //설문 상태 변경(NOT_STARTED -> IN_PROGRESS, IN_PROGRESS -> PAUSED, PAUSED -> IN_PROGRESS, IN_PROGRESS -> DONE)
    public SurveyStatusResponseDto updateSurveyStatus(Long userId, Long surveyId, SurveyStatusUpdateRequestDto requestDto) {

        User user = userFacade.findUser(userId);
        Survey survey = surveyRepository.findByIdAndIsDeletedFalse(surveyId).orElseThrow(
                () -> new CustomException(ErrorCode.SURVEY_NOT_FOUND)
        );

        currentUserMatchesSurveyCreatorOrAdmin(user, survey);

        SurveyStatus newStatus = requestDto.getStatus();

        survey.changeSurveyStatus(newStatus);

        return new SurveyStatusResponseDto(survey.getStatus());
    }

    @Transactional
    public void deleteSurvey(Long userId, Long surveyId) {

        User user = userFacade.findUser(userId);

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(ErrorCode.SURVEY_NOT_FOUND, "존재하지 않는 설문입니다.")
        );

        currentUserMatchesSurveyCreatorOrAdmin(user, survey);

        if (survey.isDeleted()) {
            throw new CustomException(ErrorCode.SURVEY_ALREADY_DELETED);
        }
        if (survey.isInProgress()) {
            throw new CustomException(ErrorCode.SURVEY_CANNOT_BE_DELETED);
        }

        List<Question> questions = questionRepository.findAllBySurvey(survey);

        for(Question q : questions){
            optionsRepository.deleteAllByQuestion(q);
        }

        questionRepository.deleteAllBySurvey(survey);

        survey.deleteSurvey();
    }


    // 참여자 API
    // 삭제 되지 않은 설문만 설문 상세 조회
    @Transactional
    public SurveyResponseDto getSurvey(Long surveyId) {

        Survey survey = surveyRepository.findByIdAndIsDeletedFalse(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));

        return surveyMapper.toResponseDto(survey);
    }

    // 설문 시작
    // survey_answer 테이블 생기면 기참여자 재참여 못하게 막는 로직 추가해야함
    @Transactional(readOnly = true)
    public SurveyQuestionDto startSurvey(Long surveyId) {

        Survey survey = surveyRepository.findByIdAndIsDeletedFalse(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));

        SurveyQuestionDto surveyQuestionDto = SurveyQuestionDto.of(survey);

        List<Question> questions = questionRepository.findAllBySurveyIdOrderByNumberASC(surveyId);

        questions.forEach(question -> {
            List<OptionResponseDto> options = optionsRepository.findAllByQuestionIdOrderByNumberAsc(question.getId());
            QuestionOptionsDto questionOptionsDto = QuestionOptionsDto.of(question, options);

            surveyQuestionDto.addQuestion(questionOptionsDto);
        });


        return surveyQuestionDto;
    }

    @Transactional
    public void saveSurveyAnswer(Long surveyId, SurveyAnswerRequestDto requestDto) {
        Survey survey = surveyRepository.findByIdAndIsDeletedFalse(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));

        Long userId = 1L;
        User user = userFacade.findUser(userId);
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        SurveyAnswer surveyAnswer = surveyAnswerRepository.save(SurveyAnswer.of(survey, user));

        requestDto.getAnswers().forEach(questionAnswer -> {
            Question question = questionRepository.findById(questionAnswer.getNumber()).orElseThrow(
                    () -> new CustomException(ErrorCode.QUESTION_NOT_FOUND)
            );

            // TODO 질문 타입에 따라 각 질문 응답을 처리할 수 있는 전략을 별도로 구성해서 처리하면 어떨까
            surveyQuestionStrategies.stream()
                    .filter(it -> it.isSupport(question.getType()))
                    .findFirst()
                    .orElseThrow()
                    .doSave(questionAnswer, surveyAnswer, question);
        });
    }

    @Transactional(readOnly = true)
    public SurveyeeSurveyListDto getSurveyeeSurveyList() {

        // User 추가시 userId 수정
        Long userId = 1L;
        User user = userFacade.findUser(userId);
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        List<SurveyAnswer> surveyAnswerList = surveyAnswerRepository.findAllByUserIdOrderByCreatedAtDesc(user);

        SurveyeeSurveyListDto surveyListDto = new SurveyeeSurveyListDto();

        surveyAnswerList.forEach(surveyAnswer -> {
            SurveyeeSurveyDto surveyeeSurveyDto = SurveyeeSurveyDto.of(surveyAnswer);

            surveyListDto.addSurveyeeSurveyDto(surveyeeSurveyDto);
        });

        return surveyListDto;
    }

    //유저가 참여자 권한일 때 예외
    public void isCurrentUserSurveyee(User user) {
        if (user.isUserRoleSurveyee()) {
            throw new CustomException(ErrorCode.SURVEYEE_NOT_ALLOWED);
        }
    }

    //해당 설문 출제자가 아니거나 관리자가 아닐 시 예외
    public void currentUserMatchesSurveyCreatorOrAdmin(User user, Survey survey) {
        if (!survey.isUserSurveyCreator(user) && user.isUserRoleNotAdmin()) {
            throw new CustomException(ErrorCode.NOT_SURVEY_CREATOR);
        }
    }

    //설문이 진행 전 상태가 아닐 때 예외
    public void isSurveyNotStarted(Survey survey) {
        if (!survey.isNotStarted()) {
            throw new CustomException(ErrorCode.SURVEY_STARTED);
        }
    }

}
