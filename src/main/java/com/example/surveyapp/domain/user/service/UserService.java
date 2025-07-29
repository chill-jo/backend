package com.example.surveyapp.domain.user.service;

import com.example.surveyapp.domain.point.domain.model.entity.Point;
import com.example.surveyapp.domain.point.domain.repository.PointRepository;
import com.example.surveyapp.domain.user.controller.dto.*;
import com.example.surveyapp.domain.user.controller.dto.RegisterRequestDto;
import com.example.surveyapp.domain.user.controller.dto.UserRequestDto;
import com.example.surveyapp.domain.user.controller.dto.UserResponseDto;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserBaseData;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import com.example.surveyapp.domain.user.domain.repository.UserBaseDataRepository;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import com.example.surveyapp.global.security.jwt.CustomUserDetails;
import com.example.surveyapp.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PointRepository pointRepository;
    private final UserBaseDataRepository userBaseDataRepository;

    @Transactional
    public void register(RegisterRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.EXISTS_EMAIL);
        }

        if (userRepository.existsByNickname(requestDto.getNickname())) {
            throw new CustomException(ErrorCode.EXISTS_NICKNAME);
        }

        if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = User.of(
                requestDto.getEmail(),
                encodedPassword,
                requestDto.getName(),
                requestDto.getNickname(),
                UserRoleEnum.SURVEYEE
        );

        userRepository.save(user);

        Point point = Point.of(user);
        pointRepository.save(point);
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmailAndIsDeletedFalse(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getUserRole());

        return LoginResponseDto.builder()
                .id(String.valueOf(user.getId()))
                .name(user.getName())
                .token(accessToken)
                .build();
    }

    @Transactional
    public void withdraw(Long userId, WithdrawRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        user.softDelete();
    }

    @Transactional(readOnly = true)
    public UserResponseDto getMyInfo(Long userId){
        User user = findUser(userId);
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto updateMyInfo(Long userId, UserRequestDto requestDto){
        User user = findUser(userId);

        validateDuplicatedUser(requestDto);

        user.updateInfo(requestDto.getEmail(), requestDto.getName(), requestDto.getNickname(), requestDto.getPassword(), passwordEncoder);

        return UserResponseDto.from(user);
    }

    private void validateDuplicatedUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.EXISTS_EMAIL);
        }

        if (userRepository.existsByNickname(userRequestDto.getNickname())) {
            throw new CustomException(ErrorCode.EXISTS_NICKNAME);
        }
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }


    // 참여자 기초 정보 작성 선택지 보여주는 메서드
    @Transactional(readOnly = true)
    public BaseDataInfoResponseDto getBaseDataInfo(CustomUserDetails userDetails) {

        User user = userRepository.findById(userDetails.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );

        List<UserBaseData> userBaseDataList = userBaseDataRepository.findAllByUserId(user);

        if (userBaseDataList.get(0).getId() != null) {
            throw new CustomException(ErrorCode.EXISTS_BASE_DATA);
        }

        return new BaseDataInfoResponseDto();
    }

    // 참여자 로그인 시 기초 정보 체크하여 없을 경우 기초정보 입력 메서드 호출
    // 참여자 기초 정보 가져오는 메서드
//    private UserBaseData getUserBaseData(CustomUserDetails user) {
//
//        user.getId();
//
//    }
}
