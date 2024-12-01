package com.example.KorkiMedic.service;

import com.example.KorkiMedic.dto.RewardDTO;
import com.example.KorkiMedic.entity.ServReward;
import com.example.KorkiMedic.repository.ServRewardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RewardService {

    private final ServRewardRepository servRewardRepository;

    public RewardService(ServRewardRepository servRewardRepository) {
        this.servRewardRepository = servRewardRepository;
    }

    // Metoda zwracająca DTO z nagrodami na podstawie id usługi
    public List<RewardDTO> getRewardsByService(Long servId) {
        List<ServReward> servRewards = servRewardRepository.findByServ_Id(servId);
        return  servRewards.stream()
                .map(this::mapToRewardDTO)
                .collect(Collectors.toList());
    }

    // Metoda mapująca encję ServReward na DTO
    private RewardDTO mapToRewardDTO(ServReward servReward) {
        RewardDTO rewardDTO = new RewardDTO();
        rewardDTO.setRewardId(servReward.getId());
        rewardDTO.setRewardName(servReward.getReward().getName());
        rewardDTO.setDescription(servReward.getReward().getDescription());
        rewardDTO.setPointsRequired(servReward.getPointsRequired());
        rewardDTO.setDiscount(servReward.getReward().getDiscount());
        return rewardDTO;
    }
}
