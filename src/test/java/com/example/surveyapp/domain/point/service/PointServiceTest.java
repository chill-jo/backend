package com.example.surveyapp.domain.point.service;


import com.example.surveyapp.domain.point.controller.dto.request.PointChargeRequestDto;
import com.example.surveyapp.domain.point.domain.model.entity.Payment;
import com.example.surveyapp.domain.point.domain.model.entity.Point;
import com.example.surveyapp.domain.point.domain.model.entity.PointHistory;
import com.example.surveyapp.domain.point.domain.repository.PaymentRepository;
import com.example.surveyapp.domain.point.domain.repository.PointHistoryRepository;
import com.example.surveyapp.domain.point.domain.repository.PointRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("서비스 : 포인트 테스트")
@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PointService pointService;


    @Test
    void 출제자_포인트를_충전한다(){

        // given
        Long userId = 1L;
        Long chargeAmount = 10000L;
        User userMock=mock(User.class);
        Point pointMock=mock(Point.class);
        PointChargeRequestDto dto = mock(PointChargeRequestDto.class);


        when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));
        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(pointMock));
        when(dto.getPrice()).thenReturn(chargeAmount);


        // when
        pointService.charge(userId,dto);

        // then
        verify(pointMock).pointCharge(chargeAmount); //충전
        verify(paymentRepository).save(any(Payment.class)); //결제 내역 저장
        verify(pointHistoryRepository).save(any(PointHistory.class)); //포인트 내역 저장
    }

//    @Test
//    void 출제자_포인트_충전_요청_값이_5000원_미만인_경우_POINT_INVALID_AMOUNT_커스템_예외_발생(){
//// given
//        Long userId = 1L;
//        Long chargeAmount = 4999L;
//        User userMock=mock(User.class);
//        Point pointMock=mock(Point.class);
//        PointChargeRequestDto dto = mock(PointChargeRequestDto.class);
//
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));
//        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(pointMock));
//        when(dto.getPrice()).thenReturn(chargeAmount);
//
//
//        //작성중
//    }

    @Test
    void 출제자_포인트_충전_요청_값이_null인_경우_POINT_INVALID_AMOUNT_커스템_예외_발생(){
        //given
        //when
        //then
    }

    @Test
    void 출제자_설문생성시_포인트가_차감된다(){
        //given
        //when
        //then
    }

    @Test
    void 출제자_설문생성시_차감_포인트보다_부족한_경우_예외_발생(){
        //given
        //when
        //then
    }

    @Test
    void 출제자_포인트_내역_조회를_성공한다(){
        //given
        //when
        //then
    }


    @Test
    void 출제자_포인트_내역_조회를_실패한다(){
        //given
        //when
        //then
    }

    @Test
    void 참여자_설문_응답시_포인트를_지급받는다(){
        //given
        //when
        //then
    }

    @Test
    void 참여자_상점_상품_교환시_상품_가격만큼_포인트가_차감된다(){
        //given
        //when
        //then
    }

    @Test
    void 참여자_상점_상품_교환시_보유_포인트가_부족한_경우_POINT_NOT_ENOUGH_커스텀_예외_발생(){
        //given
        //when
        //then
    }

    // 동시성
    // 이중 지급
    // 이중 차감





}