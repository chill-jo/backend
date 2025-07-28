package com.example.surveyapp.config;

import com.example.surveyapp.domain.product.domain.model.Product;
import com.example.surveyapp.domain.product.domain.model.Status;

public class ProductFixtureGenerator {

    public static final String TITLE = "상품명";
    public static final int PRICE = 1800;
    public static final String CONTENT = "상품설명";
    public static final Status STATUS = Status.ON_SALE;

    public static Product generateProductFixture() {
      return Product.create("상품이름이다",1800,"상품설명이다",Status.ON_SALE);

    }
}
