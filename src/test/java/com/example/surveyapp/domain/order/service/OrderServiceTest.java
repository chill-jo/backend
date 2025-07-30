package com.example.surveyapp.domain.order.service;

import com.example.surveyapp.config.OrderFixtureGenerator;
import com.example.surveyapp.config.ProductFixtureGenerator;
import com.example.surveyapp.domain.order.controller.dto.OrderCreateRequestDto;
import com.example.surveyapp.domain.order.controller.dto.OrderCreateResponseDto;
import com.example.surveyapp.domain.order.controller.dto.OrderResponseDto;
import com.example.surveyapp.domain.order.model.Order;
import com.example.surveyapp.domain.order.model.repository.OrderRepository;
import com.example.surveyapp.domain.point.domain.model.entity.Point;
import com.example.surveyapp.domain.point.domain.repository.PointRepository;
import com.example.surveyapp.domain.product.domain.model.Product;
import com.example.surveyapp.domain.product.domain.model.Status;
import com.example.surveyapp.domain.product.domain.model.repository.ProductRepository;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    private UserRepository userRepository;

    @Mock
    private PointRepository pointRepository;

    @Mock
    private ProductRepository productRepository;

    @Test
    void 주문_생성() {

        // Given
        //테스트 전제 조건 및 환경 설정
        User user = User.of("test@test.com", "Test1234!", "김도한", "도한1", UserRoleEnum.SURVEYEE);
        Long userId = 1L;

        Product product = ProductFixtureGenerator.generateProductFixture();
        Long productId = 1L;
        Order order = OrderFixtureGenerator.generateOrderFixture(user);

        //Point point = new Point(1L, user, 3000L);

        OrderCreateRequestDto requestDto = new OrderCreateRequestDto(1L, "상품명", 2500);
        // When
        //실행할 행동
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        //when(pointRepository.findByUser(user)).thenReturn(Optional.of(point));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        OrderCreateResponseDto responseDto = orderService.createOrder(requestDto, userId);

        // Then
        //검증 사항
        assertThat(responseDto.getPrice() >= order.getPrice());
        assertThat(responseDto.getPrice()).isEqualTo(order.getPrice());
        assertThat(responseDto.getStatus()).isEqualTo(Status.ON_SALE);
        assertThat(responseDto.getTitle()).isEqualTo(order.getTitle());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("관리자가 주문을 조회한다.")
    void 관리자_주문_조회() {
        // Given
        //테스트 전제 조건 및 환경 설정
        User admin = User.of("TES4@test.com", "test1123~", "김도한", "ADMIN", UserRoleEnum.ADMIN);
        User user1 = User.of("test1@test.com", "test1234!", "김민진", "민진1", UserRoleEnum.SURVEYEE);
        User user2 = User.of("test2@test.com", "test1234!", "김민성", "민성1", UserRoleEnum.SURVEYEE);
        User user3 = User.of("test3@test.com", "test1234!", "김도한", "도한1", UserRoleEnum.SURVEYEE);

        Order order1 = OrderFixtureGenerator.generateOrderFixture(user1);
        Order order2 = OrderFixtureGenerator.generateOrderFixture(user2);
        Order order3 = OrderFixtureGenerator.generateOrderFixture(user3);
        List<Order> orderList = List.of(order1, order2, order3);

        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        PageImpl<Order> orders = new PageImpl<>(orderList, pageable, orderList.size());

        when(orderRepository.findAll(any(Pageable.class))).thenReturn(orders);
        // When
        //실행할 행동
        List<OrderResponseDto> orderResponseDtos = orderService.readAllOrder(page, size);

        // Then
        //검증 사항
        assertThat(orderResponseDtos.size()).isEqualTo(orderResponseDtos.size());
        assertThat(admin.getUserRole()).isEqualTo(UserRoleEnum.ADMIN);
        assertThat(order1.getUser()).isEqualTo(user1);
        assertThat(order2.getUser()).isEqualTo(user2);
        assertThat(order3.getUser()).isEqualTo(user3);

    }

    @Test
    @DisplayName("본인의 주문 이력은 본인만 확인 할 수 있다.")
    void 본인_주문내역_확인하기() {
        // Given
        //테스트 전제 조건 및 환경 설정
        User user1 = User.of("test1@test.com", "test1234!", "김민진", "민진1", UserRoleEnum.SURVEYEE);
        Order order1 = OrderFixtureGenerator.generateOrderFixture(user1);
        Order order2 = OrderFixtureGenerator.generateOrderFixture(user1);
        Order order3 = OrderFixtureGenerator.generateOrderFixture(user1);
        List<Order> orderList1 = List.of(order1, order2, order3);

        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);

        PageImpl<Order> orders1 = new PageImpl<>(orderList1, pageable, orderList1.size());
        when(orderRepository.findByUser(user1, pageable)).thenReturn(orders1);
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        // When
        List<OrderResponseDto> response1 = orderService.readMyOrderList(page, size, user1.getId());

        // Then
        assertThat(response1.size()).isEqualTo(orderList1.size());

        //주문과 오더 id 일치하는지 검증
        for (int i = 0; i < orderList1.size(); i++) {
            Order order = orderList1.get(i);
            OrderResponseDto orderResponseDto = response1.get(i);

            assertThat(orderResponseDto.getOrderId()).isEqualTo(order.getId());
        }


    }
}