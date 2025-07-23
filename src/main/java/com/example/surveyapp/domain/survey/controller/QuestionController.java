package com.example.surveyapp.domain.survey.controller;

import com.example.surveyapp.domain.survey.service.QuestionService;
import com.example.surveyapp.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/{surveyId}")
    public ResponseEntity<ApiResponse<Void>> createQuestion(
            @PathVariable Long surveyId
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "질문이 생성되었습니다.", null));
    }

    @PatchMapping("/{surveyId}/question/{questionId}")
    public ResponseEntity<ApiResponse<Void>> updateQuestion(
            @PathVariable Long surveyId,
            @PathVariable Long questionId
    ){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "질문이 수정되었습니다.", null));
    }

    @DeleteMapping("/{surveyId}/question/{questionId}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(
            @PathVariable Long surveyId,
            @PathVariable Long questionId
    ){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "질문이 삭제되었습니다.", null));
    }
}
