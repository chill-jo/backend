package com.example.surveyapp.domain.user.controller;

import com.example.surveyapp.domain.user.controller.dto.*;
import com.example.surveyapp.domain.user.controller.dto.RegisterRequestDto;
import com.example.surveyapp.domain.user.controller.dto.UserRequestDto;
import com.example.surveyapp.domain.user.controller.dto.UserResponseDto;
import com.example.surveyapp.domain.user.service.UserService;
import com.example.surveyapp.global.response.ApiResponse;
import com.example.surveyapp.global.security.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    // 회원 정보 조회
    @GetMapping("/my-page")
    public ResponseEntity<ApiResponse<UserResponseDto>> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info(String.valueOf(userDetails.getId()));
        UserResponseDto getResponseDto = userService.getMyInfo(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("회원 정보 조회 성공", getResponseDto));
    }

    // 회원 정보 수정
    @PatchMapping("/my-page")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserRequestDto requestDto
    ) {
        log.info(String.valueOf(userDetails.getId()));
        UserResponseDto updatedResponseDto = userService.updateMyInfo(userDetails.getId(), requestDto);
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


    // 참여자 기초 정보 등록 선택지 보기
    @GetMapping("/surveyee/base-data-info")
    public ResponseEntity<ApiResponse<BaseDataInfoResponseDto>> getBaseDataInfo() {
        return ResponseEntity.ok(ApiResponse.success("선택지 중 해당하는 번호를 선택하여 입력해주세요.", userService.getBaseDataInfo()));
    }

    // 참여자 기초 정보 C,U
    @PostMapping("/surveyee/base-datas")
    public ResponseEntity<ApiResponse<Void>> saveBaseDatas(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody BaseDataListRequestDto requestDto
    ) {
        userService.saveBaseDatas(user.getId(), requestDto);
        return ResponseEntity.ok(ApiResponse.success("기초 정보가 저장되었습니다.", null));
    }

    // 참여자 기초 정보 R
    @GetMapping("/surveyee/base-datas")
    public ResponseEntity<ApiResponse<BaseDataListResponseDto>> getBaseDatas(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(ApiResponse.success("기초 정보를 조회하였습니다.", userService.getBaseDatas(user.getId())));
    }

}
