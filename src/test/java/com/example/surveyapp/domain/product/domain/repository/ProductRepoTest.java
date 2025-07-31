package com.example.surveyapp.domain.product.domain.repository;

import com.example.surveyapp.config.testbase.DataJpaTestBase;
import com.example.surveyapp.config.generator.ProductFixtureGenerator;
import com.example.surveyapp.domain.product.domain.model.Product;
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
        Product product= ProductFixtureGenerator.generateProductFixture();

        // when
        productRepository.save(product);

        // then
        Assertions.assertThat(product.getCreatedAt()).isNotNull();
    }
}
