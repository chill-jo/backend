package com.example.surveyapp.domain.user.service;

import com.example.surveyapp.domain.user.controller.dto.UserRequestDto;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User getMyInfo(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional
    public User updateMyInfo(Long userId, UserRequestDto requestDto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        validateDuplicatedUser(requestDto);

        user.updateInfo(requestDto.getEmail(), requestDto.getName(), requestDto.getNickname());

        // 추후 passwordEncoder로 변경
        if(requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()){
            String encodedPassword = new BCryptPasswordEncoder().encode(requestDto.getPassword());
            user.updatePassword(encodedPassword);
        }
        return user;
    }

    private void validateDuplicatedUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.EXISTS_EMAIL);
        }

        if (userRepository.existsByNickname(userRequestDto.getNickname())) {
            throw new CustomException(ErrorCode.EXISTS_NICKNAME);
        }
    }
}
