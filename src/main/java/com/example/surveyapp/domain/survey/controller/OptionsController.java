package com.example.surveyapp.domain.survey.controller;

import com.example.surveyapp.domain.survey.controller.dto.request.OptionCreateRequestDto;
import com.example.surveyapp.domain.survey.controller.dto.response.OptionResponseDto;
import com.example.surveyapp.domain.survey.service.OptionsService;
import com.example.surveyapp.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey/")
public class OptionsController {

    private final OptionsService optionsService;

    @PostMapping("/{surveyId}/question/{questionId}/option")
    public ResponseEntity<ApiResponse<OptionResponseDto>> createOption(
            @PathVariable Long surveyId,
            @PathVariable Long questionId,
            @RequestBody OptionCreateRequestDto requestDto){

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "선택지가 생성되었습니다.", null));
    }

    @GetMapping("/{surveyId}/question/{questionId}/option")
    public ResponseEntity<ApiResponse<List<OptionResponseDto>>> getOptions(
            @PathVariable Long surveyId,
            @PathVariable Long questionId
    ){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "선택지 목록을 조회했습니다.", null));
    }

    @PatchMapping("/{surveyId}/question/{questionId}/option/{optionId}")
    public ResponseEntity<ApiResponse<OptionResponseDto>> updateOption(
            @PathVariable Long surveyId,
            @PathVariable Long questionId,
            @PathVariable Long optionId
    ){

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "선택지가 수정되었습니다.", null));
    }

    @DeleteMapping("/{surveyId}/question/{questionId}/option/{optionId}")
    public ResponseEntity<ApiResponse<Void>> deleteOption(
            @PathVariable Long surveyId,
            @PathVariable Long questionId,
            @PathVariable Long optionId
    )
    {

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "선택지가 삭제되었습니다.", null));
    }
}
