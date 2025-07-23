package com.example.surveyapp.domain.admin.controller;

import com.example.surveyapp.domain.admin.controller.dto.UserDto;
import com.example.surveyapp.domain.admin.service.AdminService;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService AdminService;

    /**
     * 전체 회원 조회(검색)
     *
     * @param search 검색어 (필수 X)
     * @return Page<UserDTO> 유저리스트 페이징
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserDto>>> findUserList(
            @RequestParam(required = false) String search,
            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok(ApiResponse.success("전체 회원 조회되었습니다.", AdminService.findUserList(search, pageable)));
    }


    /**
     * 단일 회원 조회
     *
     * @param userId 유저 고유 식별자
     * @return User 유저 정보
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> findUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("회원 조회되었습니다.", AdminService.findUser(userId)));
    }
}
