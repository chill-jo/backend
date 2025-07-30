package com.example.surveyapp.domain.order.model;

import com.example.surveyapp.domain.product.domain.model.Product;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.global.config.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public void delete(){
        this.isDeleted = true;
    }

    @Builder(access = AccessLevel.PRIVATE)
    private Order(User user, Product product, String title, Long price) {
        this.user = user;
        this.product = product;
        this.title = title;
        this.price = price;
    }

    public static Order create(User user, Product product, String  title, Long price) {

        return Order.builder()
                .user(user)
                .product(product)
                .title(product.getTitle())
                .price(product.getPrice())
                .build();
    }
}
