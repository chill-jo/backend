package com.example.surveyapp.domain.admin.service;

import com.example.surveyapp.domain.admin.domain.model.BlackList;
import com.example.surveyapp.domain.admin.domain.repository.BlackListRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.ApiResponse;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlackListService {

    private final BlackListRepository blackListRepository;
    private final UserRepository userRepository;

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
