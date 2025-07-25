package com.example.surveyapp.domain.point.domain.repository;

import com.example.surveyapp.domain.point.domain.model.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
