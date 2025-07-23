package com.example.surveyapp.domain.admin.service;

import com.example.surveyapp.domain.admin.domain.model.BlackList;
import com.example.surveyapp.domain.admin.domain.repository.BlackListRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


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
        User blackUser = blackListService.addBlackList(userId);

        // then
        Assertions.assertThat(blackUser).isNotNull();

    }

    @Test
    void 블랙리스트_등록을_실패한다() {

        // given
        Long userId = 1L;

        // when & then
        try {
            blackListService.addBlackList(userId);
        } catch (CustomException e) {
            Assertions.assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> {
                        throw e;
                    }).withMessageContaining("해당 회원 정보가 없습니다.");
        }


    }


    @Test
    void 블랙리스트에서_삭제한다() {

        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(blackListRepository.findByUserId(user)).thenReturn(Optional.of(blackList));

        // when
        User blackUser = blackListService.deleteBlackList(userId);

        // then
        Assertions.assertThat(blackUser).isNull();

    }

    @Test
    void 블랙리스트_삭제를_실패한다1() {

        // given
        Long userId = 1L;

        // when & then
        try {
            blackListService.deleteBlackList(userId);
        } catch (CustomException e) {
            Assertions.assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> {
                        throw e;
                    }).withMessageContaining("해당 회원 정보가 없습니다.");
        }

    }

    @Test
    void 블랙리스트_삭제를_실패한다2() {

        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when & then
        try {
            blackListService.deleteBlackList(userId);
        } catch (CustomException e) {
            Assertions.assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> {
                        throw e;
                    }).withMessageContaining("해당 회원은 블랙이 아닙니다.");
        }

    }

}