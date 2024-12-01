package com.example.KorkiMedic.dto;


import com.example.KorkiMedic.entity.Serv;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String serviceName;
    private String appointmentDescription;
    private float price;
    private LocalDateTime appointmentDateTime;
    private String status;
    private boolean isDoctorAppointment;
}