package com.example.surveyapp.domain.admin.service;

import com.example.surveyapp.domain.admin.controller.dto.UserDto;
import com.example.surveyapp.domain.admin.domain.model.BlackList;
import com.example.surveyapp.domain.admin.domain.repository.BlackListRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDto userDto;

    @Mock
    private User user;

    @Mock
    private BlackListRepository blackListRepository;

    @Mock
    private BlackList blackList;

    @InjectMocks
    private AdminService adminService;


    @Test
    void 검색어를_조건으로_전체_회원을_조회한다() {

        // given
        UserRoleEnum userRole = UserRoleEnum.ADMIN;
        String search = "test";
        Pageable pageable = Pageable.unpaged();
        Page<UserDto> userList = Page.empty();
        when(userRepository.findAllBySearch(search, pageable)).thenReturn(userList);

        // when
        Page<UserDto> result = adminService.getUserList(userRole, search, pageable);

        // then
        Assertions.assertThat(userList).isEqualTo(result);

    }

    @Test
    void 권한이_없어서_전체_회원_조회를_실패한다() {

        // given
        UserRoleEnum userRole = UserRoleEnum.SURVEYEE;
        String search = "test";
        Pageable pageable = Pageable.unpaged();
        Page<UserDto> userList = Page.empty();
        when(userRepository.findAllBySearch(search, pageable)).thenReturn(userList);

        // when & then
        try {
            adminService.getUserList(userRole, search, pageable);
        } catch (CustomException e) {
            Assertions.assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> { throw new CustomException(e.getErrorCode()); })
                    .withMessageContaining("관리자 계정으로 로그인하세요.");
        }

    }


    @Test
    void 단일_회원을_조회한다() {

        // given
        UserRoleEnum userRole = UserRoleEnum.ADMIN;
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        UserDto result = adminService.getUser(userRole, userId);

        // then
        Assertions.assertThat(userDto.getId()).isEqualTo(result.getId());

    }

    @Test
    void 권한이_없어서_단일_회원_조회를_실패한다() {

        // given
        UserRoleEnum userRole = UserRoleEnum.SURVEYEE;
        Long userId = 1L;

        // when & then
        try {
            adminService.getUser(userRole, userId);
        } catch (CustomException e) {
            Assertions.assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> { throw new CustomException(e.getErrorCode()); })
                    .withMessageContaining("관리자 계정으로 로그인하세요.");
        }

    }

    @Test
    void 회원이_없어서_단일_회원을조회를_실패한다() {

        // given
        UserRoleEnum userRole = UserRoleEnum.ADMIN;
        Long userId = 1L;

        // when & then
        try {
            adminService.getUser(userRole, userId);
        } catch (CustomException e) {
            Assertions.assertThatExceptionOfType(CustomException.class)
            .isThrownBy(() -> { throw new CustomException(e.getErrorCode()); })
                    .withMessageContaining("사용자를 찾을 수 없습니다.");
        }

    }


    @Test
    void 블랙리스트에_등록한다() {

        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User result = adminService.addBlackList(userId);

        // then
        Assertions.assertThat(user).isEqualTo(result);

    }

    @Test
    void 블랙리스트_등록을_실패한다1() {

        // given
        Long userId = 1L;

        // when & then
        try {
            adminService.addBlackList(userId);
        } catch (CustomException e) {
            Assertions.assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> { throw new CustomException(e.getErrorCode()); })
                    .withMessageContaining("사용자를 찾을 수 없습니다.");
        }

    }

    @Test
    void 블랙리스트_등록을_실패한다2() {

        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when & then
        try {
            adminService.addBlackList(userId);
        } catch (CustomException e) {
            Assertions.assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> { throw new CustomException(e.getErrorCode()); })
                    .withMessageContaining("해당 회원은 이미 블랙입니다.");
        }

    }


    @Test
    void 블랙리스트에서_삭제한다() {

        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(blackListRepository.findByUserId(user)).thenReturn(Optional.of(blackList));

        // when
        User result = adminService.deleteBlackList(userId);


        // then
        Assertions.assertThat(user).isEqualTo(result);

    }

    @Test
    void 블랙리스트_삭제를_실패한다1() {

        // given
        Long userId = 1L;

        // when & then
        try {
            adminService.deleteBlackList(userId);
        } catch (CustomException e) {
            Assertions.assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> { throw new CustomException(e.getErrorCode()); })
                    .withMessageContaining("사용자를 찾을 수 없습니다.");
        }

    }

    @Test
    void 블랙리스트_삭제를_실패한다2() {

        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when & then
        try {
            adminService.deleteBlackList(userId);
        } catch (CustomException e) {
            Assertions.assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> { throw new CustomException(e.getErrorCode()); })
                    .withMessageContaining("해당 회원은 블랙이 아닙니다.");
        }

    }
}