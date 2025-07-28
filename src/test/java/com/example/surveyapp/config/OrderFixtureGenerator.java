package com.example.surveyapp.config;

import com.example.surveyapp.domain.order.model.Order;
import com.example.surveyapp.domain.product.domain.model.Product;
import com.example.surveyapp.domain.product.domain.model.Status;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;


public class OrderFixtureGenerator {

    public static Order generateOrderFixture(User user) {

        Product product = Product.create("상품명",2500,"상품설명",Status.ON_SALE);

        return Order.create(user,product, product.getTitle(), product.getPrice());

    }
}
