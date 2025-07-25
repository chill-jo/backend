package com.example.surveyapp.domain.admin.controller;

import com.example.surveyapp.domain.admin.controller.dto.SurveyeeStatsDto;
import com.example.surveyapp.domain.admin.controller.dto.UserDto;
import com.example.surveyapp.domain.admin.service.AdminService;
import com.example.surveyapp.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    // 전체 회원 조회(검색)
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserDto>>> getUserList(
            @RequestParam(required = false) String search,
            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok(ApiResponse.success("전체 회원 조회되었습니다.", adminService.getUserList(search, pageable)));
    }

    // 단일 회원 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("회원 조회되었습니다.", adminService.getUser(userId)));
    }

    // 분류별 참여자 통계 조회
    @GetMapping("/stats/users")
    public ResponseEntity<ApiResponse<List<SurveyeeStatsDto>>> getSurveyeeStats(
            @RequestParam(required = false, defaultValue = "1999-01-01 00:00:00") String startDate,
            @RequestParam(required = false, defaultValue = "2999-01-01 00:00:00") String endDate
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateLocal = LocalDateTime.parse(startDate, formatter);
        LocalDateTime endDateLocal = LocalDateTime.parse(endDate, formatter);

        return ResponseEntity.ok(ApiResponse.success("분류별 참여자 통계 조회하였습니다.", adminService.getSurveyeeStats(startDateLocal, endDateLocal)));

    }
}
