package com.example.surveyapp.domain.user.service;

import com.example.surveyapp.domain.user.controller.dto.UserRequestDto;
import com.example.surveyapp.domain.user.controller.dto.UserResponseDto;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.surveyapp.domain.user.config.UserFixtureGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void 유저_정보를_조회한다(){
        // Given
        when(userRepository.findById(ID)).thenReturn(Optional.of(generateUserFixture()));

        // When
        UserResponseDto result = userService.getMyInfo(ID);

        // Then
        assertThat(result.getEmail()).isEqualTo(EMAIL);
        assertThat(result.getName()).isEqualTo(NAME);
        assertThat(result.getNickname()).isEqualTo(NICKNAME);
        assertThat(result.getUserRole()).isEqualTo(ROLE);
    }

    @Test
    void 유저_정보를_수정한다(){
        // Given
        User user = generateUserFixture();
        UserRequestDto requestDto = new UserRequestDto("newEmail@example.com", "newPassword", "NewName", "newNickname");

        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByNickname(requestDto.getNickname())).thenReturn(false);

        // When
        UserResponseDto updatedUser = userService.updateMyInfo(ID, requestDto);

        // Then
        assertThat(updatedUser.getEmail()).isEqualTo(requestDto.getEmail());
        assertThat(updatedUser.getName()).isEqualTo(requestDto.getName());
        assertThat(updatedUser.getNickname()).isEqualTo(requestDto.getNickname());
    }

    @Test
    void 유저_수정_도중_이메일이_중복되었다(){
        // Given
        User user = generateUserFixture();
        UserRequestDto requestDto = new UserRequestDto("duplicate@example.com", "password", "name", "nickname");

        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.updateMyInfo(ID, requestDto))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.EXISTS_EMAIL.getMessage());
    }
}
