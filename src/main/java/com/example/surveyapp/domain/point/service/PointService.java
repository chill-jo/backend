package com.example.surveyapp.domain.point.service;

import com.example.surveyapp.domain.point.controller.dto.request.PointChargeRequestDto;
import com.example.surveyapp.domain.point.domain.model.entity.Payment;
import com.example.surveyapp.domain.point.domain.model.entity.Point;
import com.example.surveyapp.domain.point.domain.model.entity.PointHistory;
import com.example.surveyapp.domain.point.domain.model.enums.*;
import com.example.surveyapp.domain.point.domain.repository.PaymentRepository;
import com.example.surveyapp.domain.point.domain.repository.PointHistoryRepository;
import com.example.surveyapp.domain.point.domain.repository.PointRepository;
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


    @Transactional
    public void charge(Long userId, PointChargeRequestDto dto){
        //요청받은 금액
        Long price=dto.getPrice();

        //포인트 조회.
        Point point = pointRepository.findById(userId)
                .orElseGet(() -> new Point(0L, null, userId));

        //포인트 충전
        point.PointCharge(price+point.getPointBalance());

        // pointRepository에 저장
        pointRepository.save(point);

        PointHistory history = new PointHistory(
                null,
                point.getPointBalance(),
                price,
                point.getPointBalance()+price,
                PointType.CHARGE,
                Target.PAYMENTS,
                null,
                "포인트 충전",point.getUser(),  null);

        pointHistoryRepository.save(history);

        Payment payment = new Payment(
                null,
                history,
                point.getPointBalance(),
                UUID.randomUUID(),
                Status.DONE,
                Method.KAKAO_PAY,
                TargetType.POINT_CHARGE
        );
        paymentRepository.save(payment);
    }



}
