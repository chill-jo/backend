package com.example.surveyapp.config;

import com.example.surveyapp.domain.order.model.Order;
import com.example.surveyapp.domain.product.domain.model.Product;
import com.example.surveyapp.domain.product.domain.model.Status;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import org.springframework.test.util.ReflectionTestUtils;


public class OrderFixtureGenerator {

    public static Order generateOrderFixture(User user,Product product) {

        Order order = Order.create(user, product);
        Long id = 1L;
        ReflectionTestUtils.setField(order,"id",id);
        return order;

    }
}
