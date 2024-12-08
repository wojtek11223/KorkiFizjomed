package com.example.KorkiMedic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserPointActionDTO {
    private String pointActionName;
    private Integer points;
    private LocalDateTime actionDate;
}
