package com.example.surveyapp.domain.point.domain.model.entity;

import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.global.config.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
public class Point  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(nullable = false)
    private Long pointBalance;

    @Builder(access = AccessLevel.PRIVATE)
    private Point(User user){
        this.user=user;
        this.pointBalance=0L;
    }

    public static Point of(User user){
        return Point.builder()
                .user(user)
                .build();
    }

    public void pointCharge(Long amount) {
        this.pointBalance+=amount;
    }

    public void earn(Long amount) {
        this.pointBalance+=amount;
    }

    public void redeem(Long amount){
        this.pointBalance-=amount;
    }

}
