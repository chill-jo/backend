package com.example.surveyapp.domain.product.service;

import com.example.surveyapp.domain.product.controller.dto.ProductCreateRequestDto;
import com.example.surveyapp.domain.product.controller.dto.ProductCreateResponseDto;
import com.example.surveyapp.domain.product.model.Product;
import com.example.surveyapp.domain.product.model.repository.ProductRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;


    /**
     * @param dto         생성 요청 DTO
     * @param userDetails 인증된 사용자 정보 가져오기(관리자만)
     * @return
     * @PreAuthorize("hashRole('ADMIN')") ADMIN 관리자만 생성 할 수 있도록
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductCreateResponseDto createProduct(ProductCreateRequestDto dto, UserDetails userDetails) {

        if (userDetails == null) {
            throw new CustomException(ErrorCode.NOT_ADMIN_USER_ERROR);
        }

        Product product = Product.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .build();

        Product saved = productRepository.save(product);
        return ProductCreateResponseDto.from(saved);

    }
}