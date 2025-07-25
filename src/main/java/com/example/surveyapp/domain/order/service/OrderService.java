package com.example.surveyapp.domain.order.service;

import com.example.surveyapp.domain.order.controller.dto.OrderCreateRequestDto;
import com.example.surveyapp.domain.order.controller.dto.OrderCreateResponseDto;
import com.example.surveyapp.domain.order.model.repository.OrderRepository;
import com.example.surveyapp.domain.product.model.Status;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;


    public OrderCreateResponseDto createOrder(@Valid OrderCreateRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (userId == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
        if (!userId.equals(UserRoleEnum.SURVEYEE)){
            throw new CustomException(ErrorCode.NOT_ORDER_USER);
        }
        if (requestDto.getPrice() =<  requestDto.getUserId())

    }
}
