package com.example.surveyapp.domain.admin.controller;

import com.example.surveyapp.domain.admin.service.BlackListService;
import com.example.surveyapp.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class BlackListController {

    private final BlackListService blackListService;

    /**
     * 블랙리스트 등록
     *
     * @param userId 유저 Id
     * @return user 블랙리스트 등록된 유저 정보
     */
    @PostMapping("/black/{userId}")
    public ResponseEntity<ApiResponse<HttpStatus>> addBlackList(@PathVariable Long userId) {
        return ResponseEntity.ok(blackListService.addBlackList(userId));
    }


    /**
     * 블랙리스트 삭제
     *
     * @param userId 유저 Id
     * @return user 블랙리스트 삭제된 유저 정보
     */
    @DeleteMapping("/black/{userId}")
    public ResponseEntity<ApiResponse<HttpStatus>> deleteBlackList(@PathVariable Long userId) {
        return ResponseEntity.ok(blackListService.deleteBlackList(userId));
    }

}
