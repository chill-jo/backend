package com.example.surveyapp.domain.product.service;

import com.example.surveyapp.domain.product.controller.dto.ProductCreateRequestDto;
import com.example.surveyapp.domain.product.controller.dto.ProductCreateResponseDto;
import com.example.surveyapp.domain.product.controller.dto.ProductResponseDto;
import com.example.surveyapp.domain.product.controller.dto.ProductUpdateRequestDto;
import com.example.surveyapp.domain.product.model.Product;
import com.example.surveyapp.domain.product.model.Status;
import com.example.surveyapp.domain.product.model.repository.ProductRepository;
import com.example.surveyapp.domain.product.service.dto.ProductUpdateResponseDto;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import com.example.surveyapp.domain.user.domain.repository.UserRepository;
import com.example.surveyapp.global.response.exception.CustomException;
import com.example.surveyapp.global.response.exception.ErrorCode;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    /**
     * @param dto         생성 요청 DTO
     * @param userId 인증된 사용자 정보 가져오기(관리자만)
     * @return
     * @PreAuthorize("hashRole('ADMIN')") ADMIN 관리자만 생성 할 수 있도록
     */
    @Transactional
    public ProductCreateResponseDto createProduct(ProductCreateRequestDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (user.getUserRole() != UserRoleEnum.ADMIN) {
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

    /**
     *
     * @param page
     * @param size
     * @return
     */
    public List<ProductResponseDto> readAllProduct(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> products = productRepository.findAllByStatusAndIsDeletedFalse(Status.ON_SALE,pageable);

        List<Product> productList = products.getContent();

        return productList.stream()
                .map(ProductResponseDto::from)
                .toList();
    }


    public ProductResponseDto readOneProduct(Long id) {

        Product product= productRepository.findByIdAndStatusAndIsDeletedFalse(id,Status.ON_SALE)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
        return ProductResponseDto.from(product);
    }

    @Transactional
    public ProductUpdateResponseDto updateProduct(Long id, ProductUpdateRequestDto requestDto) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

//        if (user.getUserRole() != UserRoleEnum.ADMIN) {
//            throw new CustomException(ErrorCode.NOT_ADMIN_USER_ERROR);
//        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        if (!product.getTitle().equals(requestDto.getTitle())) {
            boolean onlyOne = productRepository.existsByTitleAndIsDeletedFalse(requestDto.getTitle());
            if (onlyOne){
                throw new CustomException(ErrorCode.NOT_SAME_PRODUCT_TITLE);
            }

            if (requestDto.getStatus() == null) {
                throw new CustomException(ErrorCode.NOT_FOUND_PRODUCT_STATUS);
            }
        }

         product.update(
                requestDto.getTitle(),
                requestDto.getPrice(),
                requestDto.getContent(),
                requestDto.getStatus());



        return new ProductUpdateResponseDto(
                product.getId(),
                product.getTitle(),
                product.getContent(),
                product.getPrice(),
                product.getStatus()
        );

    }

    @Transactional
    public void deleteProduct(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (user.getUserRole() != UserRoleEnum.ADMIN) {
            throw new CustomException(ErrorCode.NOT_ADMIN_USER_ERROR);
        }
        Product product = productRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        product.delete();
    }
}