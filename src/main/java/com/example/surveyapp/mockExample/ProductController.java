package com.example.surveyapp.mockExample;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {
    // 상품 ID로 단건 조회 (mock 인증 포함)
    @GetMapping("{id}")
    ResponseEntity<String> getProductById(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 인증된 사용자 정보 로그로 출력 (디버깅용)
        log.info("userDetails username: {}, roles: {}", userDetails.getUsername(), userDetails.getAuthorities());

        // 일단은 상품 ID를 문자열로 응답 (예제용)
        return ResponseEntity.ok(String.valueOf(id));
    }
}
