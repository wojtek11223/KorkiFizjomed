package com.example.KorkiMedic.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AppointmentDetailsDTO {
    private String serviceDescription;
    private String phoneNumber;
    private Set<String> specializations;
    private String rewardName;
}
