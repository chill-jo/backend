package com.example.surveyapp.domain.point.domain.model.entity;


import com.example.surveyapp.domain.point.domain.model.enums.PointType;
import com.example.surveyapp.domain.point.domain.model.enums.Target;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.global.config.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class PointHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long currentBalance;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private Long afterBalance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Target target;

    @Column(nullable = false)
    private Long targetId;

    @Column(length = 255, nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "pointHistory")
    private Payment payment;
}
