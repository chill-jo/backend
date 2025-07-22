package com.example.surveyapp.domain.survey.controller;

import com.example.surveyapp.domain.survey.dto.request.SurveyCreateRequestDto;
import com.example.surveyapp.domain.survey.dto.request.SurveyUpdateRequestDto;
import com.example.surveyapp.domain.survey.service.SurveyService;
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
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createSurvey(@RequestBody @Valid SurveyCreateRequestDto requestDto){

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "설문이 생성되었습니다.", null));
    }

    //설문 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<Void>> getSurveys(){

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "설문 목록을 조회했습니다.", null));
    }

    //설문 상세정보 수정
    @PatchMapping("/{surveyId}")
    public ResponseEntity<ApiResponse<Void>> updateSurvey(@Valid @RequestBody SurveyUpdateRequestDto requestDto){

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "설문이 수정되었습니다.", null));
    }

    //설문 상태 변경(NOT_STARTED -> IN_PROGRESS, IN_PROGRESS -> PAUSED, PAUSED -> IN_PROGRESS, IN_PROGRESS -> DONE)
    @PatchMapping("/{surveyId}/status")
    public ResponseEntity<ApiResponse<Void>> updateSurveyStatus(){

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "설문 상태가 변경되었습니다.", null));
    }

    //설문 삭제
    @DeleteMapping("/{surveyId}")
    public ResponseEntity<ApiResponse<Void>> deleteSurvey(){

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "설문이 삭제되었습니다.", null));
    }

    //설문 통계 조회
    @GetMapping("/{surveyId}")
    public ResponseEntity<ApiResponse<Void>> getSurveyStatistics(){

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "설문 통계를 조회했습니다.", null));
    }
}
