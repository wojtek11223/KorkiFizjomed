package com.example.KorkiMedic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardDTO {
    private Long rewardId;
    private String rewardName;
    private String description;
    private float pointsRequired;
    private float discount;  // Wartość zniżki powiązana z usługą
}
