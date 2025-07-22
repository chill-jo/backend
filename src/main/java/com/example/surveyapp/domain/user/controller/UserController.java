package com.example.surveyapp.domain.user.controller;

import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.service.UserService;
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

    @GetMapping("/my-page")
    public ResponseEntity<ResponseDto<Map<String, Object>>> getMyInfo(
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        User user = userService.getMyInfo(principal.getUserId());
        return ResponseEntity.ok(new ResponseDto<>("회원 정보 조회 성공", userData(user)));
    }

    @PatchMapping("/my-page")
    public ResponseEntity<ResponseDto<Map<String, Object>>> updateMyInfo(
            @AuthenticationPrincipal CustomPrincipal principal,
            @Valid @RequestBody UserRequestDto requestDto
    ) {
        User user = userService.updateMyInfo(principal.getUserId(), requestDto);
        return ResponseEntity.ok(new ResponseDto<>("회원 정보 수정 성공", userData(user)));
    }
}
