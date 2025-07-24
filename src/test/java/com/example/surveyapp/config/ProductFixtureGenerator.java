package com.example.surveyapp.config;

import com.example.surveyapp.domain.product.model.Product;
import com.example.surveyapp.domain.product.model.Status;

public class ProductFixtureGenerator {

    public static final String TITLE = "상품명";
    public static final int PRICE = 1800;
    public static final String CONTENT = "상품설명";
    public static final Status STATUS = Status.ON_SALE;

    public static Product generateProductFixture() {
      return Product.builder()
               .title(TITLE)
               .price(PRICE)
               .content(CONTENT)
               .status(STATUS)
               .build();
    }
}
