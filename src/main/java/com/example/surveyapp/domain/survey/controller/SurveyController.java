package com.example.surveyapp.domain.survey.controller;

import com.example.surveyapp.domain.survey.controller.dto.request.SurveyAnswerRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyCreateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyStatusUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.request.SurveyUpdateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.response.PageSurveyResponseDto;
import com.example.surveyapp.domain.survey.controller.dto.response.SurveyQuestionDto;
import com.example.surveyapp.domain.survey.controller.dto.response.SurveyResponseDto;
import com.example.surveyapp.domain.survey.controller.dto.response.SurveyStatusResponseDto;
import com.example.surveyapp.domain.survey.service.SurveyService;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyController {

    private final SurveyService surveyService;

    //설문 생성
    //***User 부분 인증인가 추가 후 수정해야함!!***
    @PostMapping
    public ResponseEntity<ApiResponse<SurveyResponseDto>> createSurvey(@RequestBody @Valid SurveyCreateRequestDto requestDto, User user){
        SurveyResponseDto responseDto = surveyService.createSurvey(requestDto, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "설문이 생성되었습니다.", responseDto));
    }

    //설문 목록 조회(정렬 없이 삭제된 설문만 제외)
    @GetMapping
    public ResponseEntity<ApiResponse<PageSurveyResponseDto<SurveyResponseDto>>> getSurveys(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){

        PageSurveyResponseDto<SurveyResponseDto> pagedSurveys = surveyService.getSurveys(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "설문 목록을 조회했습니다.", pagedSurveys));
    }

    //설문 상세정보 수정
    @PatchMapping("/{surveyId}")
    public ResponseEntity<ApiResponse<SurveyResponseDto>> updateSurvey(
            @PathVariable Long surveyId,
            @Valid @RequestBody SurveyUpdateRequestDto requestDto){

        SurveyResponseDto responseDto = surveyService.updateSurvey(surveyId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "설문이 수정되었습니다.", responseDto));
    }

    //설문 상태 변경(NOT_STARTED -> IN_PROGRESS, IN_PROGRESS -> PAUSED, PAUSED -> IN_PROGRESS, IN_PROGRESS -> DONE)
    @PatchMapping("/{surveyId}/status")
    public ResponseEntity<ApiResponse<SurveyStatusResponseDto>> updateSurveyStatus(
            @PathVariable Long surveyId,
            @Valid @RequestBody SurveyStatusUpdateRequestDto requestDto
    ){

        SurveyStatusResponseDto responseDto = surveyService.updateSurveyStatus(surveyId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "설문 상태가 변경되었습니다.", responseDto));
    }

    //설문 삭제
    @DeleteMapping("/{surveyId}")
    public ResponseEntity<ApiResponse<Void>> deleteSurvey(
            @PathVariable Long surveyId
            ){

        surveyService.deleteSurvey(surveyId);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "설문이 삭제되었습니다.", null));
    }

    //설문 통계 조회
    @GetMapping("/{surveyId}/result")
    public ResponseEntity<ApiResponse<Void>> getSurveyStatistics(){

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "설문 통계를 조회했습니다.", null));
    }


    // 참여자 API
    // 설문 상세 조회
    @GetMapping("/{surveyId}")
    public ResponseEntity<ApiResponse<SurveyResponseDto>> getSurvey(@PathVariable Long surveyId) {

        return ResponseEntity.ok(ApiResponse.success("설문 조회하였습니다.", surveyService.getSurvey(surveyId)));
    }

    // 설문 시작
    @GetMapping("/{surveyId}/start")
    public ResponseEntity<ApiResponse<SurveyQuestionDto>> startSurvey(@PathVariable Long surveyId) {

        return ResponseEntity.ok(ApiResponse.success("설문을 시작합니다.", surveyService.startSurvey(surveyId)));
    }

    // 설문 응답 제출
    @PostMapping("/{surveyId}/answer")
    public ResponseEntity<ApiResponse<Void>> answerSurvey(
            @PathVariable Long surveyId,
            @RequestBody @Valid SurveyAnswerRequestDto requestDto
//            @AuthenticationPrincipal UserDetails user
    ) {

//        surveyService.saveSurveyAnswer(surveyId, requestDto, user.userId);
        surveyService.saveSurveyAnswer(surveyId, requestDto);

        return ResponseEntity.ok(ApiResponse.success("설문이 완료되었습니다.", null));
    }
}
