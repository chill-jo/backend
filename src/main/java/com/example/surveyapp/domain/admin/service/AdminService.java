package com.example.surveyapp.domain.admin.service;

import com.example.surveyapp.domain.admin.controller.dto.StatDto;
import com.example.surveyapp.domain.admin.controller.dto.StatsListDto;
import com.example.surveyapp.domain.admin.controller.dto.UserDto;
import com.example.surveyapp.domain.admin.domain.model.BlackList;
import com.example.surveyapp.domain.admin.domain.repository.BlackListRepository;
import com.example.surveyapp.domain.user.domain.model.CategoryEnum;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import com.example.surveyapp.domain.user.domain.repository.UserBaseDataRepository;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserBaseDataRepository userBaseDataRepository;
    private final BlackListRepository blackListRepository;

    @Transactional(readOnly = true)
    public Page<UserDto> getUserList(UserRoleEnum userRole, String search, Pageable pageable) {
        if (!userRole.equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.NOT_ADMIN_USER_ERROR);
        }

        return userRepository.findAllBySearch(search, pageable);
    }

    @Transactional(readOnly = true)
    public UserDto getUser(UserRoleEnum userRole, Long userId) {
        if (!userRole.equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.NOT_ADMIN_USER_ERROR);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return UserDto.from(user);
    }

    @Transactional(readOnly = true)
    public List<StatsListDto> getStats(UserRoleEnum userRole, LocalDateTime startDateLocal, LocalDateTime endDateLocal) {
        if (!userRole.equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.NOT_ADMIN_USER_ERROR);
        }

        List<CategoryEnum> categoryEnumList = Arrays.stream(CategoryEnum.values()).toList();

        return categoryEnumList.stream().map(
                categoryEnum -> {
                    StatsListDto statsListDto = StatsListDto.of(categoryEnum.getCategory());
                    for (Long i = 1L; i <= categoryEnum.getOptionMaxNum(); i++) {
                        Long count = userBaseDataRepository.countByCategoryAndDataAndStartDateAndEndDate(categoryEnum, i, startDateLocal, endDateLocal);
                        StatDto statDto = StatDto.of(i, count);
                        statsListDto.addStat(statDto);
                    }
                    return statsListDto;
                }
        ).toList();

    }

    @Transactional
    public User addBlackList(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );

        if (blackListRepository.findByUserId(user).isPresent()) {
            throw new CustomException(ErrorCode.IS_BLACKLIST);
        }

        blackListRepository.save(new BlackList(user));

        return user;
    }


    @Transactional
    public User deleteBlackList(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );

        BlackList blackList = blackListRepository.findByUserId(user).orElseThrow(
                () -> new CustomException(ErrorCode.IS_NOT_BLACKLIST)
        );

        blackListRepository.delete(blackList);

        return user;
    }

}
