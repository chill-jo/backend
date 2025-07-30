package com.example.surveyapp.config;

import com.example.surveyapp.domain.product.domain.model.Product;
import com.example.surveyapp.domain.product.domain.model.Status;

public class ProductFixtureGenerator {

    public static final String TITLE = "상품명";
    public static final Long PRICE = 2500L;
    public static final String CONTENT = "상품설명";
    public static final Status STATUS = Status.ON_SALE;

    public static Product generateProductFixture() {
        Product product = Product.create(TITLE, PRICE, CONTENT, STATUS);
        product.updateId(1L);
        return product;

    }
}
