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


@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;


    // 충전
    @Transactional
    public void charge(Long userId, PointChargeRequestDto dto){
        //요청받은 금액
        Long price=dto.getPrice();

        
        //사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        
        //포인트 조회
        Point point = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.POINT_NOT_FOUND));

        //충전 전 금액
        Long currentBalance=point.getPointBalance();

        //포인트 충전. (dirty checking)
        point.pointCharge(price);

        PointHistory history = PointHistory.of(
                currentBalance,
                price,
                point.getPointBalance(),
                PointType.CHARGE,
                Target.PAYMENTS,
                null,
                "포인트 충전",
                user,
                point
        );

        pointHistoryRepository.save(history);

        Payment payment = Payment.of(
                price,
                PointStatus.DONE,
                Method.KAKAO_PAY,
                TargetType.POINT_CHARGE
        );
        paymentRepository.save(payment);
    }

    // 설문 응답하는 경우 적립
    @Transactional
    public void earn(Long userId, Long amount, Long surveyAnswerId){

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 포인트 조회
        Point point = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.POINT_NOT_FOUND));

        //적립 전 포인트
        Long currentBalance=point.getPointBalance();

        //포인트 적립 (dirty checking)
        point.earn(amount);

        //포인트 내역 기록
        PointHistory history = PointHistory.of(
                currentBalance,
                amount,
                point.getPointBalance(),
                PointType.EARN,
                Target.SURVEY,
                surveyAnswerId,
                "설문 응답 포인트 적립",
                user,
                point
        );

        pointHistoryRepository.save(history);
    }


    // 상점에서 상품 교환하는 경우 차감
    @Transactional
    public void redeem(Long userId, Long amount, Long orderId){

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 포인트 조회
        Point point = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.POINT_NOT_FOUND));

        //차감 전 포인트
        Long currentBalance=point.getPointBalance();


        //포인트 차감 (dirty checking)
        point.redeem(amount);

        //포인트 내역 기록
        PointHistory history = PointHistory.of(
                currentBalance,
                amount,
                point.getPointBalance(),
                PointType.USAGE,
                Target.ORDER,
                orderId,
                "상품 교환 포인트 차감",
                user,
                point
        );

        pointHistoryRepository.save(history);
    }

}
