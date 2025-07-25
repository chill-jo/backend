package com.example.surveyapp.domain.point.controller;

import com.example.surveyapp.domain.point.controller.dto.request.PointChargeRequestDto;
import com.example.surveyapp.domain.point.service.PointService;
import com.example.surveyapp.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @PostMapping("/points")
    public ResponseEntity<ApiResponse<Void>> create(@Valid @RequestBody PointChargeRequestDto dto,
                                                                     @RequestParam("userId") Long userId) {
        pointService.charge(userId,dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("결제가 되었습니다.",null));
    }


}
