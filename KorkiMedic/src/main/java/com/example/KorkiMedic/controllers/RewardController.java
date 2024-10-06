package com.example.KorkiMedic.controllers;

import com.example.KorkiMedic.dto.RewardDTO;
import com.example.KorkiMedic.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    // Endpoint zwracający listę nagród powiązanych z usługą w formie DTO
    @GetMapping("/{servId}")
    public ResponseEntity<List<RewardDTO>> getRewardsByService(@PathVariable Long servId) {
        List<RewardDTO> rewards = rewardService.getRewardsByService(servId);
        return ResponseEntity.ok(rewards);
    }
}
