package com.example.surveyapp.domain.order.service;

import com.example.surveyapp.config.OrderFixtureGenerator;
import com.example.surveyapp.domain.order.controller.dto.OrderCreateRequestDto;
import com.example.surveyapp.domain.order.controller.dto.OrderCreateResponseDto;
import com.example.surveyapp.domain.order.model.Order;
import com.example.surveyapp.domain.order.model.repository.OrderRepository;
import com.example.surveyapp.domain.point.domain.model.entity.Point;
import com.example.surveyapp.domain.point.domain.repository.PointRepository;
import com.example.surveyapp.domain.product.model.Status;
import com.example.surveyapp.domain.product.model.repository.ProductRepository;
import com.example.surveyapp.domain.product.service.ProductService;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("서비스 : 주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PointRepository pointRepository;

    @Test
    void 주문_생성() {

        // Given
        //테스트 전제 조건 및 환경 설정
        User user = User.of("test@test.com", "Test1234!", "김도한", "도한1", UserRoleEnum.SURVEYEE);
        Long userId = 1L;

        Order order = OrderFixtureGenerator.generateOrderFixture();

        Point point = new Point(1L, user, 3000L);

        OrderCreateRequestDto requestDto = new OrderCreateRequestDto(1L, "상품명", 2500);
        // When
        //실행할 행동
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pointRepository.findByUser(user)).thenReturn(Optional.of(point));

        OrderCreateResponseDto responseDto = orderService.createOrder(requestDto, userId);

        // Then
        //검증 사항
        assertThat(responseDto.getPrice() >= order.getPrice());
        assertThat(responseDto.getPrice()).isEqualTo(order.getPrice());
        assertThat(responseDto.getStatus()).isEqualTo(order.getProduct().getStatus());
        assertThat(responseDto.getTitle()).isEqualTo(order.getTitle());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}