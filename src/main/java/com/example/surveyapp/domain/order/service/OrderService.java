package com.example.surveyapp.domain.order.service;

import com.example.surveyapp.domain.order.controller.dto.OrderCreateRequestDto;
import com.example.surveyapp.domain.order.controller.dto.OrderCreateResponseDto;
import com.example.surveyapp.domain.order.model.Order;
import com.example.surveyapp.domain.order.model.repository.OrderRepository;
import com.example.surveyapp.domain.point.domain.model.entity.Point;
import com.example.surveyapp.domain.point.domain.repository.PointRepository;
import com.example.surveyapp.domain.product.model.Status;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private PointRepository pointRepository;


    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Point point = pointRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POINT));

        if (!user.isUserRoleSurveyee()){
            throw new CustomException(ErrorCode.NOT_ORDER_USER); //참여자 계정이 아니면 주문 생성 불가
        }
        if (point.getPointBalance() < requestDto.getPrice()) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_POINT);
        }

        Order order = Order.builder()
                .id(requestDto.getProductId())
                .user(user)
                .title(requestDto.getTitle())
                .price(requestDto.getPrice())
                .build();

        //포인트 차감 로직 추가 예정

        Order saved = orderRepository.save(order);

        return OrderCreateResponseDto.from(saved);
    }
}
