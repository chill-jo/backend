package com.example.surveyapp.domain.user.controller;

import com.example.surveyapp.domain.user.controller.dto.*;
import com.example.surveyapp.domain.user.controller.dto.RegisterRequestDto;
import com.example.surveyapp.domain.user.controller.dto.UserRequestDto;
import com.example.surveyapp.domain.user.controller.dto.UserResponseDto;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.service.UserService;
import com.example.surveyapp.global.response.ApiResponse;
import com.example.surveyapp.global.security.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    // 인증인가 도입 후 AuthenticationPrincipal로 변경
    @GetMapping("/my-page")
    public ResponseEntity<ApiResponse<UserResponseDto>> getMyInfo(
            @RequestParam("userId") Long userId
    ) {
        UserResponseDto getResponseDto = userService.getMyInfo(userId);
        return ResponseEntity.ok(ApiResponse.success("회원 정보 조회 성공", getResponseDto));
    }

    // "
    @PatchMapping("/my-page")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateMyInfo(
            @RequestParam("userId") Long userId,
            @Valid @RequestBody UserRequestDto requestDto
    ) {
        UserResponseDto updatedResponseDto = userService.updateMyInfo(userId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("회원 정보 수정 성공", updatedResponseDto));
    }

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @RequestBody @Valid RegisterRequestDto requestDto
    ) {
        userService.register(requestDto);
        return ResponseEntity.ok(ApiResponse.success("회원가입 성공", null));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @RequestBody @Valid LoginRequestDto requestDto
    ) {
        LoginResponseDto response = userService.login(requestDto);
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", response));
    }

    //회원탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal CustomUserDetails userDetails, // 로그인 유저 정보
            @RequestBody @Valid WithdrawRequestDto requestDto
    ) {
        userService.withdraw(userDetails.getId(), requestDto);
        return ResponseEntity.ok(ApiResponse.success("회원 탈퇴 완료", null));
    }

}
