package com.example.surveyapp.domain.admin.service;

import com.example.surveyapp.domain.admin.domain.model.BlackList;
import com.example.surveyapp.domain.admin.domain.repository.BlackListRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.ApiResponse;
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
    public ApiResponse<HttpStatus> addBlackList(Long userId) {

        User blackUser = userRepository.findById(userId).orElse(null);

        if (blackUser == null) {
            return ApiResponse.fail(ErrorCode.BLACKLIST_BAD_REQUEST1.getMessage(), ErrorCode.BLACKLIST_BAD_REQUEST1.getStatus());
        }

        if (blackListRepository.findByUserId(blackUser).isPresent()) {
            return ApiResponse.fail(ErrorCode.BLACKLIST_BAD_REQUEST2.getMessage(), ErrorCode.BLACKLIST_BAD_REQUEST2.getStatus());
        }

        blackListRepository.save(new BlackList(blackUser));

        return ApiResponse.success("블랙리스트에 등록되었습니다.", HttpStatus.OK);
    }


    @Transactional
    public ApiResponse<HttpStatus> deleteBlackList(Long userId) {

        User blackUser = userRepository.findById(userId).orElse(null);

        if (blackUser == null) {
            return ApiResponse.fail(ErrorCode.BLACKLIST_BAD_REQUEST1.getMessage(), ErrorCode.BLACKLIST_BAD_REQUEST1.getStatus());
        }

        BlackList blackList = blackListRepository.findByUserId(blackUser).orElse(null);

        if (blackList == null) {
            return ApiResponse.fail(ErrorCode.BLACKLIST_BAD_REQUEST3.getMessage(), ErrorCode.BLACKLIST_BAD_REQUEST3.getStatus());
        }

        blackListRepository.delete(blackList);

        return ApiResponse.success("블랙리스트에서 삭제되었습니다.", HttpStatus.OK);
    }

}
