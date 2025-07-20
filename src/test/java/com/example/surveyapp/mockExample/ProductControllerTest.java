package com.example.surveyapp.mockExample;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "안지현", roles = "ADMIN")
    @DisplayName("내가 등록한 상품 상세 조회")
    void getProducts() throws Exception {
        // Given
        Long productId = 1L;

        // When
        // localhost:8080/api/products/1
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{id}", productId)
        );

        // Then
        resultActions.andDo(print());
    }
}
