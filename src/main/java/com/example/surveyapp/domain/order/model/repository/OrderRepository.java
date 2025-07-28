package com.example.surveyapp.domain.order.model.repository;

import com.example.surveyapp.domain.order.model.Order;
import com.example.surveyapp.domain.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUser(User user, Pageable pageable);
}
