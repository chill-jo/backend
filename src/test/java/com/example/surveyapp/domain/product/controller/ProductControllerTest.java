package com.example.surveyapp.domain.product.controller;

import com.example.surveyapp.domain.product.controller.dto.ProductCreateRequestDto;
import com.example.surveyapp.domain.product.controller.dto.ProductCreateResponseDto;
import com.example.surveyapp.domain.product.controller.dto.ProductResponseDto;
import com.example.surveyapp.domain.product.controller.dto.ProductUpdateRequestDto;
import com.example.surveyapp.domain.product.model.Status;
import com.example.surveyapp.domain.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;


import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("상품을 생성한다")
    @WithMockUser(username = "김도한", roles = "ADMIN")
    void 상품_생성한다() throws Exception {
        // Given
        String title = "상품 이름";
        int price = 1800;
        String content = "상품 설명";
        Status status = Status.ON_SALE;

        ProductCreateRequestDto requestDto = new ProductCreateRequestDto(title, content, price, status);
        ProductCreateResponseDto responseDto = new ProductCreateResponseDto(1L,title, price, status);

        when(productService.createProduct(any(ProductCreateRequestDto.class), eq(1L))).thenReturn(responseDto);
        // When
        //실행할 행동
        ResultActions actions = mockMvc.perform(post("/api/products")
                .param("userId","1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // Then
        //검증 사항
        verify(productService,times(1)).createProduct(any(ProductCreateRequestDto.class),eq(1L));

        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value(title));

    }

    @Test
    @DisplayName("상품을 전체 조회한다.")
    @WithMockUser(username = "김도한", roles = "ADMIN")
    void 상품_전체_조회를_한다() throws Exception {
        // Given
        ProductResponseDto product1 = new ProductResponseDto(1L, "상품1", 2000, Status.ON_SALE);
        ProductResponseDto product2 = new ProductResponseDto(2L, "상품2", 2500, Status.ON_SALE);
        List<ProductResponseDto> productList = List.of(product1, product2);

        when(productService.readAllProduct(0,10)).thenReturn(productList);

        // When
        //실행할 행동
        ResultActions actions = mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productList)));
        // Then
        //검증 사항

        verify(productService, times(1)).readAllProduct(0,10);

          actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("상품1"))
                .andExpect(jsonPath("$.data[1].title").value("상품2"));
    }

    @Test
    @DisplayName("상품을 한개 조회한다.")
    @WithMockUser(username = "김도한", roles = "ADMIN")
    void 상품_한개를_조회한다() throws Exception{
        // Given
        //테스트 전제 조건 및 환경 설정
        Long id = 1L;
        ProductResponseDto product1 = new ProductResponseDto(1L, "상품1", 2000, Status.ON_SALE);

        when(productService.readOneProduct(id)).thenReturn(product1);

        // When
        //실행할 행동
        ResultActions actions = mockMvc.perform(get("/api/products/{id}",id)
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)));

        // Then
        //검증 사항
        verify(productService, times(1)).readOneProduct(id);
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("상품1"));

    }

    @Test
    @DisplayName("상품을 수정 한다.")
    @WithMockUser(username = "김도한", roles = "ADMIN")
    void 상품을_수정한다() throws Exception{
        // Given
        //테스트 전제 조건 및 환경 설정
        Long productId = 1L;
        Long userId = 1L;
        ProductUpdateRequestDto requestDto = new ProductUpdateRequestDto("변경된 상품명", 2500, "변경된 상품설명", Status.ON_SALE);

        doNothing().when(productService).updateProduct(eq(productId), any(ProductUpdateRequestDto.class));

        // When
        //실행할 행동
        ResultActions actions = mockMvc.perform(patch("/api/products/{id}", productId)
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // Then
        //검증 사항
        verify(productService,times(1))
                .updateProduct(eq(productId), any(ProductUpdateRequestDto.class));
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("변경된 상품명"));
    }

    @Test
    @DisplayName("상품을 삭제 한다.")
    @WithMockUser(username = "김도한", roles = "ADMIN")
    void 상품을_삭제한다() throws Exception{

    }
}