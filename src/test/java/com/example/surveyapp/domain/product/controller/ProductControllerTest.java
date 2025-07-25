package com.example.surveyapp.domain.product.controller;

import com.example.surveyapp.domain.product.controller.dto.ProductCreateRequestDto;
import com.example.surveyapp.domain.product.controller.dto.ProductCreateResponseDto;
import com.example.surveyapp.domain.product.controller.dto.ProductResponseDto;
import com.example.surveyapp.domain.product.controller.dto.ProductUpdateRequestDto;
import com.example.surveyapp.domain.product.model.Status;
import com.example.surveyapp.domain.product.model.repository.ProductRepository;
import com.example.surveyapp.domain.product.service.ProductService;
import com.example.surveyapp.domain.product.service.dto.ProductUpdateResponseDto;
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
@AutoConfigureMockMvc(addFilters = false) //인증인가 시 변경
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;

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

//    @Test
//    @DisplayName("참여자 계정은 생성이 불가능하다")
//    @WithMockUser(username = "김도한", roles = "SURVEYEE")
//    void 상품_생성_참여자_계정은_불가능_하다() throws Exception{
//        // Given
//        ProductCreateRequestDto requestDto = new ProductCreateRequestDto("상품", "설명", 1800, Status.ON_SALE);
//        ProductCreateResponseDto responseDto = new ProductCreateResponseDto(1L,"상품",1800  , Status.ON_SALE);
//
//        when(productService.createProduct(any(requestDto))
//        // When
//        //실행할 행동
//        ResultActions actions = mockMvc.perform(post("/api/products")
//                .param("userId","1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(requestDto)));
//
//        // Then
//        //검증 사항
//        verify(productService, never()).createProduct(any(),anyLong()); //서비스 호출 안되게
//        actions.andDo(print())
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @DisplayName("출제자 계정은 생성이 불가능하다")
//    @WithMockUser(username = "김도한", roles = "SURVEYOR")
//    void 상품_생성_출제자_계정은_불가능_하다() throws Exception{
//        // Given
//        String title = "상품 이름";
//        int price = 1800;
//        String content = "상품 설명";
//        Status status = Status.ON_SALE;
//
//        ProductCreateRequestDto requestDto = new ProductCreateRequestDto(title, content, price, status);
//        ProductCreateResponseDto responseDto = new ProductCreateResponseDto(1L,title, price, status);
//
//        when(productService.createProduct(any(ProductCreateRequestDto.class), eq(1L))).thenReturn(responseDto);
//        // When
//        //실행할 행동
//        ResultActions actions = mockMvc.perform(post("/api/products")
//                .param("userId","1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(requestDto)));
//
//        // Then
//        //검증 사항
//        actions.andDo(print())
//                .andExpect(status().isOk());
//    }

    @Test
    @DisplayName("관리자 계정으로 상품을 전체 조회한다.")
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
//
//    @Test
//    @DisplayName("출제자 계정으로 상품 조회는 불가능하다.")
//    @WithMockUser(username = "김도한", roles = "SURVEYOR")
//    void 상품_전체_조회는_출제자계정은_불가능해야한다() throws Exception {
//        // Given
//        ProductResponseDto product1 = new ProductResponseDto(1L, "상품1", 2000, Status.ON_SALE);
//        ProductResponseDto product2 = new ProductResponseDto(2L, "상품2", 2500, Status.ON_SALE);
//        List<ProductResponseDto> productList = List.of(product1, product2);
//
//        when(productService.readAllProduct(0,10)).thenReturn(productList);
//
//        // When
//        //실행할 행동
//        ResultActions actions = mockMvc.perform(get("/api/products")
//                .param("page", "0")
//                .param("size", "10")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(productList)));
//        // Then
//        //검증 사항
//        actions.andDo(print())
//                .andExpect(status().isOk());
//
//    }

    @Test
    @DisplayName("참여자 계정으로 상품 조회는 가능하다.")
    @WithMockUser(username = "김도한", roles = "SURVEYEE")
    void 상품_전체_조회_참여자계정은_가능해야한다() throws Exception {
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
    @WithMockUser(username = "김도한", roles = "SURVEYEE")
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
        ProductUpdateRequestDto requestDto = new ProductUpdateRequestDto("변경된 상품명", 2500, "변경된 상품설명", Status.ON_SALE);
        ProductUpdateResponseDto responseDto = new ProductUpdateResponseDto(1L, "변경된 상품명", "변경된 상품설명:", 2500, Status.ON_SALE);

        when(productService.updateProduct(eq(productId), any(ProductUpdateRequestDto.class))).thenReturn(responseDto);

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

//    @Test
//    @DisplayName("상품수정은 참여자가 불가능하다.")
//    @WithMockUser(username = "김도한", roles = "SURVEYEE")
//    void 상품수정_참여자는_불가능하다() throws Exception{
//        // Given
//        //테스트 전제 조건 및 환경 설정
//        Long productId = 1L;
//        ProductUpdateRequestDto requestDto = new ProductUpdateRequestDto("변경된 상품명", 2500, "변경된 상품설명", Status.ON_SALE);
//        ProductUpdateResponseDto responseDto = new ProductUpdateResponseDto(1L, "변경된 상품명", "변경된 상품설명:", 2500, Status.ON_SALE);
//
//        when(productService.updateProduct(eq(productId), any(ProductUpdateRequestDto.class))).thenReturn(responseDto);
//
//        // When
//        //실행할 행동
//        ResultActions actions = mockMvc.perform(patch("/api/products/{id}", productId)
//                .param("userId", "1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(requestDto)));
//
//        // Then
//        //검증 사항
//        actions.andDo(print())
//                .andExpect(status().isOk());
//    }

    @Test
    @DisplayName("상품을 삭제 한다.")
    @WithMockUser(username = "김도한", roles = "ADMIN")
    void 상품을_삭제한다() throws Exception{

        // Given
        //테스트 전제 조건 및 환경 설정
        Long productId = 1L;
        Long userId = 1L;

        ProductCreateRequestDto requestDto = new ProductCreateRequestDto("상품명", "상품설명", 2500, Status.ON_SALE);
        ProductCreateResponseDto responseDto = new ProductCreateResponseDto(1L, "상품명", 2500, Status.ON_SALE);

        doNothing().when(productService).deleteProduct(productId,userId);

        // When
        //실행할 행동
        ResultActions actions = mockMvc.perform(delete("/api/products/{id}", productId)
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));


        // Then
        //검증 사항
        verify(productService, times(1)).deleteProduct(productId,userId);
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());


    }
}