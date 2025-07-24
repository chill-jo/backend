package com.example.surveyapp.domain.admin.controller;

import com.example.surveyapp.domain.admin.service.BlackListService;
import com.example.surveyapp.domain.user.domain.model.User;
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
    public ResponseEntity<ApiResponse<User>> addBlackList(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("블랙리스트에 등록되었습니다.", blackListService.addBlackList(userId)));
    }


    /**
     * 블랙리스트 삭제
     *
     * @param userId 유저 Id
     * @return user 블랙리스트 삭제된 유저 정보
     */
    @DeleteMapping("/black/{userId}")
    public ResponseEntity<ApiResponse<User>> deleteBlackList(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("블랙리스트에서 삭제되었습니다.", blackListService.deleteBlackList(userId)));
    }

}
