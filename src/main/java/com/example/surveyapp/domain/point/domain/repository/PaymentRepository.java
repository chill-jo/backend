package com.example.surveyapp.domain.point.domain.repository;

import com.example.surveyapp.domain.point.domain.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
