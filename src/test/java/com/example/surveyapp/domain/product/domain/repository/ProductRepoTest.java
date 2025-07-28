package com.example.surveyapp.domain.product.domain.repository;

import com.example.surveyapp.config.DataJpaTestBase;
import com.example.surveyapp.domain.product.domain.model.Product;
import com.example.surveyapp.domain.product.domain.model.Status;
import com.example.surveyapp.domain.product.domain.model.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductRepoTest extends DataJpaTestBase {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void 상품_생성() {
        // given
        String title = "title";
        int price = 1000;
        String content = "content";
        Status status = Status.ON_SALE;

        Product product = Product.create(title, price, content, status);

        // when
        productRepository.save(product);

        // then
        Assertions.assertThat(product.getCreatedAt()).isNotNull();
    }
}
