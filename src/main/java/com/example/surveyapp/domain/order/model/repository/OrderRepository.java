package com.example.surveyapp.domain.order.model.repository;

import com.example.surveyapp.domain.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
