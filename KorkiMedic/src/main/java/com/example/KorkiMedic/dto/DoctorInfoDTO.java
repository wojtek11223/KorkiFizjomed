package com.example.KorkiMedic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorInfoDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Set<String> specializations;
    private List<ServiceDTO> services;

}
