package com.example.surveyapp.domain.point.domain.model.entity;


import com.example.surveyapp.domain.point.domain.model.enums.Method;
import com.example.surveyapp.domain.point.domain.model.enums.TargetType;
import com.example.surveyapp.domain.product.model.Status;
import com.example.surveyapp.global.config.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "point_history_id", nullable = false)
    private PointHistory pointHistory;

    @Column(nullable = false)
    private Long amount;

    @Column(unique = true, nullable = false)
    private UUID paymentKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Method method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;
}
