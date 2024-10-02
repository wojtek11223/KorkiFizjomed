package com.example.KorkiMedic.repository;

import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.entity.PointAction;
import com.example.KorkiMedic.entity.UserPointAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface UserPointActionRepository extends JpaRepository<UserPointAction, Long> {

    boolean existsByUserAndPointActionAndActionDateBetween(User user, PointAction pointAction, LocalDateTime start, LocalDateTime end);
}
