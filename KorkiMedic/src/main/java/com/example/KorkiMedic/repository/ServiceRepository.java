package com.example.KorkiMedic.repository;
import com.example.KorkiMedic.entity.Serv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ServiceRepository extends JpaRepository<Serv, Long> {
    Optional<Serv> findByName(String name);
}