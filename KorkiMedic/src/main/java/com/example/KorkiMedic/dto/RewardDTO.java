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
    private Integer pointsRequired;
    private Integer discount;  // Wartość zniżki powiązana z usługą
}
