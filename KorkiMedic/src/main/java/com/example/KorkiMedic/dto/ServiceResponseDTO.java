package com.example.KorkiMedic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean assigned;

}