package com.example.KorkiMedic.repository;

import com.example.KorkiMedic.entity.ServReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServRewardRepository extends JpaRepository<ServReward, Long> {
    List<ServReward> findByServ_Id(Long servId);
}
