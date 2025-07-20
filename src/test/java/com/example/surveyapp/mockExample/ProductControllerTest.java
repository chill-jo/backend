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

@WebMvcTest(controllers = ProductController.class) // ProductController만 테스트용으로 로딩
@AutoConfigureMockMvc(addFilters = false) // Security 필터 적용 안함 (mock 인증만 사용)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc; // API 요청 테스트 도구

    @Test
    @WithMockUser(username = "안지현", roles = "ADMIN") // 가짜 사용자로 인증된 상태를 시뮬레이션
    @DisplayName("내가 등록한 상품 상세 조회") // 테스트 설명 (테스트 리포트에서 보임)
    void getProducts() throws Exception {
        // Given
        Long productId = 1L;

        // When: /api/products/1 호출
        // localhost:8080/api/products/1
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{id}", productId)
        );

        // Then: 결과 콘솔에 출력
        resultActions.andDo(print());
    }
}
