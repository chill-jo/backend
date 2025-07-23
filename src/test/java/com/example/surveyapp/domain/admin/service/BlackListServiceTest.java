package com.example.surveyapp.domain.admin.service;

import com.example.surveyapp.domain.admin.domain.model.BlackList;
import com.example.surveyapp.domain.admin.domain.repository.BlackListRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.ApiResponse;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;


import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlackListServiceTest {


    @Mock
    private BlackListRepository blackListRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @Mock
    private BlackList blackList;

    @InjectMocks
    private BlackListService blackListService;

    @Test
    void 블랙리스트에_등록한다() {

        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        ApiResponse<HttpStatus> result = blackListService.addBlackList(userId);

        // then
        Assertions.assertThat(result.getMessage()).isEqualTo("블랙리스트에 등록되었습니다.");

    }

    @Test
    void 블랙리스트_등록을_실패한다1() {

        // given
        Long userId = 1L;

        // when
        ApiResponse<HttpStatus> result = blackListService.addBlackList(userId);

        // then
        Assertions.assertThat(result.getMessage()).isEqualTo(ErrorCode.BLACKLIST_BAD_REQUEST1.getMessage());

    }

    @Test
    void 블랙리스트_등록을_실패한다2() {

        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(blackListRepository.findByUserId(user)).thenReturn(Optional.of(blackList));

        // when
        ApiResponse<HttpStatus> result = blackListService.addBlackList(userId);

        // then
        Assertions.assertThat(result.getMessage()).isEqualTo(ErrorCode.BLACKLIST_BAD_REQUEST2.getMessage());

    }


    @Test
    void 블랙리스트에서_삭제한다() {

        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(blackListRepository.findByUserId(user)).thenReturn(Optional.of(blackList));

        // when
        ApiResponse<HttpStatus> result = blackListService.deleteBlackList(userId);


        // then
        Assertions.assertThat(result.getMessage()).isEqualTo("블랙리스트에서 삭제되었습니다.");

    }

    @Test
    void 블랙리스트_삭제를_실패한다1() {

        // given
        Long userId = 1L;

        // when
        ApiResponse<HttpStatus> result = blackListService.deleteBlackList(userId);


        // then
        Assertions.assertThat(result.getMessage()).isEqualTo(ErrorCode.BLACKLIST_BAD_REQUEST1.getMessage());

    }

    @Test
    void 블랙리스트_삭제를_실패한다2() {

        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        ApiResponse<HttpStatus> result = blackListService.deleteBlackList(userId);


        // then
        Assertions.assertThat(result.getMessage()).isEqualTo(ErrorCode.BLACKLIST_BAD_REQUEST3.getMessage());

    }

}