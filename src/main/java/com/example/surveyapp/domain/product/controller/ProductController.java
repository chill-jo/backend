package com.example.surveyapp.domain.product.controller;

import com.example.surveyapp.domain.product.controller.dto.ProductCreateRequestDto;
import com.example.surveyapp.domain.product.controller.dto.ProductCreateResponseDto;
import com.example.surveyapp.domain.product.controller.dto.ProductResponseDto;
import com.example.surveyapp.domain.product.model.Product;
import com.example.surveyapp.domain.product.service.ProductService;
import com.example.surveyapp.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductCreateResponseDto>> create(@Valid @RequestBody ProductCreateRequestDto dto,
                                                                        @RequestParam("userId") Long userId) {
        ProductCreateResponseDto product =  productService.createProduct(dto,userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("상품이 생성되었습니다.",product));
    }

    /**
     * 상품 전체 조회
     * 참여자,관리자만 조회 가능
     * 출제자는 상품 자체 조회 불가능
     */

    @GetMapping
    @PreAuthorize("hasRole('ADMIN,SURVEYEE')")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getReadAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        List<ProductResponseDto> products = productService.readAllProduct(page,size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("전체 상품을 조회하였습니다.",products));
    }
//
//    @GetMapping("{id}")
//    @PreAuthorize("hasRole('ADMIN,SURVEYEE')")
//    public ResponseEntity<ApiResponse<ProductResponseDto>> getReadOne(@PathVariable Long id) {
//       ProductResponseDto product = productService.readOneProduct(id);
//       return ResponseEntity.status(HttpStatus.OK)
//               .body(ApiResponse.success("상품을 조회하였습니다.",product));
//    }
}
