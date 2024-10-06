package com.example.KorkiMedic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class DoctorAppointmentsDTO {
    Long id;
    private String doctorFirstName;
    private String doctorLastName;
    private List<String> specializations;
    private List<ServiceDTO> services;
    private List<AppointmentInfo> appointments;

    @Data
    @AllArgsConstructor
    public static class AppointmentInfo {
        private LocalDateTime date;
        private String description;
    }
}
