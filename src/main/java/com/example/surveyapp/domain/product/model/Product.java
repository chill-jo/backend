package com.example.surveyapp.domain.product.model;

import com.example.surveyapp.domain.product.controller.dto.ProductResponseDto;
import com.example.surveyapp.domain.product.service.dto.ProductUpdateResponseDto;
import com.example.surveyapp.global.config.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product")
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String title;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 255)
    private String content;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    public void delete() {
        this.isDeleted = true;
    }

    public void update(String title, Integer price, String content, Status status) {
        if (title != null) this.title = title;
        if (price != null)  this.price = price;
        if (content != null)  this.content = content;
        if (status != null)  this.status = status;
    }
}