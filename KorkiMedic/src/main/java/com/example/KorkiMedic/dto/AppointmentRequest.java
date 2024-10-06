package com.example.KorkiMedic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    private Long doctorId;
    private Long serviceName;
    private LocalDateTime date;
    private String description;
    private Long rewardId;
}
