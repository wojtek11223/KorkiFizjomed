package com.example.KorkiMedic.repository;

import com.example.KorkiMedic.entity.PointAction;
import com.example.KorkiMedic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PointActionRepository extends JpaRepository<PointAction, Long> {
    PointAction findByReason(String reason);
}
