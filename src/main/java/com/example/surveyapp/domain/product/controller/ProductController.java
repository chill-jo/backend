package com.example.surveyapp.domain.product.controller;

import com.example.surveyapp.domain.ProductService;
import com.example.surveyapp.domain.product.controller.dto.ProductCreateRequestDto;
import com.example.surveyapp.domain.product.controller.dto.ProductCreateResponseDto;
import com.example.surveyapp.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductCreateResponseDto>> create(@RequestBody ProductCreateRequestDto dto) {

    }
}
