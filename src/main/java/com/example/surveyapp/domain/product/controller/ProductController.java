package com.example.surveyapp.domain.product.controller;

import com.example.surveyapp.domain.product.controller.dto.ProductCreateRequestDto;
import com.example.surveyapp.domain.product.controller.dto.ProductCreateResponseDto;
import com.example.surveyapp.domain.product.service.ProductService;
import com.example.surveyapp.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<ApiResponse<ProductCreateResponseDto>> create(@Valid @RequestBody ProductCreateRequestDto dto,
                                                                        @AuthenticationPrincipal UserDetails userDetails) {
        ProductCreateResponseDto product =  productService.createProduct(dto,userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("상품이 생성되었습니다.",product));
    }

}
