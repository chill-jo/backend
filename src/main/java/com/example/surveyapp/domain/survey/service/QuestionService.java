package com.example.surveyapp.domain.survey.service;

import com.example.surveyapp.domain.survey.controller.dto.request.QuestionCreateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.QuestionUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.response.PageQuestionResponseDto;
import com.example.surveyapp.domain.survey.controller.dto.response.PageSurveyResponseDto;
import com.example.surveyapp.domain.survey.controller.dto.response.QuestionResponseDto;
import com.example.surveyapp.domain.survey.domain.model.entity.Question;
import com.example.surveyapp.domain.survey.domain.model.entity.Survey;
import com.example.surveyapp.domain.survey.domain.model.enums.SurveyStatus;
import com.example.surveyapp.domain.survey.domain.repository.QuestionRepository;
import com.example.surveyapp.domain.survey.domain.repository.SurveyRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;

    //참여자 권한 막기(생성, 수정, 삭제, 진행중아닌 설문선택지조회)
    public void isCurrentUserSurveyee(Long userId, String message){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if(user.isUserRoleSurveyee()){
            throw new CustomException(ErrorCode.SURVEYEE_NOT_ALLOWED, message);
        }
    }

    //해당 설문 출제자가 아니거나 관리자가 아닐 시 예외
    public void currentUserMatchesSurveyCreator(Long userId, Survey survey, String errorMessage){
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if(!survey.isUserSurveyCreator(user) && user.isUserRoleNotAdmin()){
            throw new CustomException(ErrorCode.NOT_SURVEY_CREATOR, errorMessage);
        }
    }

    //설문 상태가 진행 전이 아닐 때 생성, 수정, 삭제 시 예외
    public void isSurveyNotStarted(Survey survey, String errorMessage){
        if(!survey.isSurveyStatusNotStarted()){
            throw new CustomException(ErrorCode.SURVEY_NOT_STARTED, errorMessage);
        }
    }

    //설문에 포함된 질문이 아닐 때 예외
    public void isQuestionFromSurvey(Survey survey, Question question){
        if(!question.isFromSurvey(survey)){
            throw new CustomException(ErrorCode.QUESTION_NOT_FROM_SURVEY);
        }
    }

    public QuestionResponseDto createQuestion(Long userId, Long surveyId, QuestionCreateRequestDto requestDto){

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));

        isCurrentUserSurveyee(userId, "참여자 권한으로는 질문을 생성할 수 없습니다.");
        currentUserMatchesSurveyCreator(userId, survey, "설문 출제자와 관리자만 질문을 생성할 수 있습니다.");
        isSurveyNotStarted(survey, "설문이 진행 전 상태일 때만 질문을 생성할 수 있습니다.");

        Question question = new Question(
                survey,
                requestDto.getNumber(),
                requestDto.getContent(),
                requestDto.getType()
        );

        questionRepository.save(question);

        return new QuestionResponseDto(question.getId(), question.getNumber(), question.getContent(), question.getType());
    }

    //질문단건조회 (필요한가??)
    //(inprogress일때는 모두, 다른 상태에는 관리자랑 출제자만 조회가능) - 구현됨
    //이미 참여한 설문인지 확인해야함. - 응답 도메인 생성 후 수정
    public QuestionResponseDto getQuestion(Long userId, Long surveyId, Long questionId){
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        isQuestionFromSurvey(survey, question);

        if(!survey.isSurveyStatusInProgress()){
            isCurrentUserSurveyee(userId, "진행 중 상태가 아닌 설문의 질문은 설문 참여자 권한으로 조회할 수 없습니다.");
            currentUserMatchesSurveyCreator(userId, survey, "설문 출제자와 관리자만 진행 중이 아닌 설문의 질문을 조회할 수 있습니다.");
        }

        return new QuestionResponseDto(question.getId(), question.getNumber(), question.getContent(), question.getType());
    }

    //질문목록조회
    //(inprogress일때는 모두, 다른 상태에는 관리자랑 출제자만 조회가능)- 구현됨,
    //in progress이고 유저가 참여자권한인 경우 이미 참여했는지 확인해야함. - 응답 도메인 생성 후 수정
    public PageQuestionResponseDto<QuestionResponseDto> getQuestions(int page, int size, Long userId, Long surveyId){
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if(!survey.isSurveyStatusInProgress()){
            isCurrentUserSurveyee(userId, "진행 중 상태가 아닌 설문의 질문은 설문 참여자 권한으로 조회할 수 없습니다.");
            currentUserMatchesSurveyCreator(userId, survey, "설문 출제자와 관리자만 진행 중이 아닌 설문의 질문 목록을 조회할 수 있습니다.");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questionPage = questionRepository.findAllBySurveyId(surveyId, pageable);

        Page<QuestionResponseDto> questionResponseDtoPage = questionPage.map(question -> new QuestionResponseDto(
                question.getId(),
                question.getNumber(),
                question.getContent(),
                question.getType()
        ));

        return new PageQuestionResponseDto<>(questionResponseDtoPage);

    }

    public QuestionResponseDto updateQuestion(Long userId, Long surveyId, Long questionId, QuestionUpdateRequestDto requestDto){

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        isCurrentUserSurveyee(userId, "참여자 권한으로는 질문을 수정할 수 없습니다.");
        currentUserMatchesSurveyCreator(userId, survey, "설문 출제자와 관리자만 질문을 수정할 수 있습니다.");
        isSurveyNotStarted(survey, "설문이 진행 전 상태일 때만 질문을 수정할 수 있습니다.");
        isQuestionFromSurvey(survey, question);

        if(requestDto.getNumber() != null){
            question.changeNumber(requestDto.getNumber());
        }
        if(requestDto.getContent() != null){
            question.changeContent(requestDto.getContent());
        }
        //주관식 -> 선택형 or 선택형 -> 주관식으로 바꿨을 때 선택지가 필수적으로 추가되거나 삭제되어야하는데
        //어떻게 하지?
        if(requestDto.getType() != null){
            question.changeQuestionType(requestDto.getType());
        }

        questionRepository.save(question);

        return new QuestionResponseDto(question.getId(), question.getNumber(), question.getContent(), question.getType());

    }

    @Transactional
    public void deleteQuestion(Long userId, Long surveyId, Long questionId){

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        isCurrentUserSurveyee(userId, "참여자 권한으로는 질문을 삭제할 수 없습니다.");
        currentUserMatchesSurveyCreator(userId, survey, "설문 출제자와 관리자만 질문을 삭제할 수 있습니다.");
        isSurveyNotStarted(survey, "설문이 진행 전 상태일 때만 질문을 삭제할 수 있습니다.");
        isQuestionFromSurvey(survey, question);

        questionRepository.delete(question);
    }
}
