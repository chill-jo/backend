package com.example.surveyapp.domain.order.controller;

import com.example.surveyapp.domain.order.controller.dto.OrderCreateRequestDto;
import com.example.surveyapp.domain.order.controller.dto.OrderCreateResponseDto;
import com.example.surveyapp.domain.order.service.OrderService;
import com.example.surveyapp.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderCreateResponseDto>> create(@Valid@RequestBody OrderCreateRequestDto requestDto,
                                                                      @RequestParam("userId")Long userId) {

        OrderCreateResponseDto order = orderService.createOrder(requestDto,userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("주문이 완료되었습니다.",order));
    }

}
