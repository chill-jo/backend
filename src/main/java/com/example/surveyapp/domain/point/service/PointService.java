package com.example.surveyapp.domain.point.service;

import com.example.surveyapp.domain.point.controller.dto.request.PointChargeRequestDto;
import com.example.surveyapp.domain.point.domain.model.entity.Payment;
import com.example.surveyapp.domain.point.domain.model.entity.Point;
import com.example.surveyapp.domain.point.domain.model.entity.PointHistory;
import com.example.surveyapp.domain.point.domain.model.enums.*;
import com.example.surveyapp.domain.point.domain.repository.PaymentRepository;
import com.example.surveyapp.domain.point.domain.repository.PointHistoryRepository;
import com.example.surveyapp.domain.point.domain.repository.PointRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;


    @Transactional
    public void charge(Long userId, PointChargeRequestDto dto){
        //요청받은 금액
        Long price=dto.getPrice();

        
        //사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        
        //포인트 조회.
        Point point = pointRepository.findByUserId(userId)
                .orElseGet(() -> new Point(user));

        //충전 전 금액
        Long currentBalance=point.getPointBalance();

        //포인트 충전
        point.pointCharge(price);

        // pointRepository에 저장
        pointRepository.save(point);

        PointHistory history = new PointHistory(
                null,
                currentBalance,
                price,
                point.getPointBalance(),
                PointType.CHARGE,
                Target.PAYMENTS,
                null,
                "포인트 충전",user, null);

        pointHistoryRepository.save(history);

        Payment payment = new Payment(
                null,
                history,
                price,
                UUID.randomUUID(),
                Status.DONE,
                Method.KAKAO_PAY,
                TargetType.POINT_CHARGE
        );
        paymentRepository.save(payment);
    }



}
