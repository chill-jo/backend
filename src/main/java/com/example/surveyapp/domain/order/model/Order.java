package com.example.surveyapp.domain.order.model;

import com.example.surveyapp.domain.product.model.Product;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.global.config.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

}
