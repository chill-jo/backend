package com.example.surveyapp.domain.admin.service;

import com.example.surveyapp.domain.admin.domain.model.BlackList;
import com.example.surveyapp.domain.admin.domain.repository.BlackListRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class BlackListService {

    BlackListRepository blackListRepository;
    UserRepository userRepository;

    public User addBlackList(Long userId) {

        User blackUser = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.BLACKLIST_BAD_REQUEST));

        BlackList blackList = new BlackList(blackUser);

        blackListRepository.save(blackList);

        return blackList.getUserId();
    }


    public User deleteBlackList(Long userId) {

        User blackUser = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.BLACKLIST_BAD_REQUEST));

        BlackList blackList = blackListRepository.findByUserId(blackUser)
                .orElseThrow(()-> new CustomException(ErrorCode.BLACKLIST_BAD_REQUEST, "해당 회원은 블랙이 아닙니다."));

        blackListRepository.delete(blackList);

        return blackList.getUserId();
    }

}
