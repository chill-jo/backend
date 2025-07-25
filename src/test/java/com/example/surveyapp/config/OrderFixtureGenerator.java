package com.example.surveyapp.config;

import com.example.surveyapp.domain.order.controller.dto.OrderCreateRequestDto;
import com.example.surveyapp.domain.order.model.Order;
import com.example.surveyapp.domain.product.model.Product;
import com.example.surveyapp.domain.product.model.Status;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import org.springframework.security.core.parameters.P;


public class OrderFixtureGenerator {

    public static Order generateOrderFixture() {
        User user = User.of("test@test.com", "test1234!", "김도한", "도한1", UserRoleEnum.SURVEYEE);
        Product product = Product.builder()
                .title("상품명")
                .content("상품설명")
                .price(2500)
                .status(Status.ON_SALE)
                .build();

        return Order.builder()
                .product(product)
                .user(user)
                .build();
    }
}
