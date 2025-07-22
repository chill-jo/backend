package com.example.surveyapp.domain.product.model.repository;

import com.example.surveyapp.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
