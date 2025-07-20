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
    @GetMapping("{id}")
    ResponseEntity<String> getProductById(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        log.info("userDetails username: {}, roles: {}", userDetails.getUsername(), userDetails.getAuthorities());
        return ResponseEntity.ok(String.valueOf(id));
    }
}
