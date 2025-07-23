package com.example.surveyapp.domain.product.model.repository;

import com.example.surveyapp.domain.product.controller.dto.ProductResponseDto;
import com.example.surveyapp.domain.product.model.Product;
import com.example.surveyapp.domain.product.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Page<Product> findAllByStatusAndIsDeletedFalse(Status status, Pageable pageable);

    Product findByOneStatusAndDeletedFalse(Status status, Long id);
}
