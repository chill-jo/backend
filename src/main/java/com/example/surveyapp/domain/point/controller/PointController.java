package com.example.surveyapp.domain.point.controller;

import com.example.surveyapp.domain.point.controller.dto.request.PointChargeRequestDto;
import com.example.surveyapp.domain.point.controller.dto.response.PointHistoryResponseDto;
import com.example.surveyapp.domain.point.service.PointService;
import com.example.surveyapp.global.response.PageResponse;
import com.example.surveyapp.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    //포인트 충전
    @PostMapping("/charge")
    public ResponseEntity<ApiResponse<Void>> charge(@Valid @RequestBody PointChargeRequestDto dto,
                                                                     @RequestParam("userId") Long userId) {
        pointService.charge(userId,dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("결제가 되었습니다.",null));
    }

    //포인트 조회
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PointHistoryResponseDto>>> getHistories(
            @RequestParam("userId") Long userId, Pageable pageable){
        Page<PointHistoryResponseDto> page = pointService.getHistories(userId,pageable);
        return ResponseEntity.ok(ApiResponse.success("포인트 내역이 조회되었습니다.",new PageResponse<>(page)));
    }


}
