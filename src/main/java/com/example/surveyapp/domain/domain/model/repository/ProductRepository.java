package com.example.surveyapp.domain.domain.model.repository;

import com.example.surveyapp.domain.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
