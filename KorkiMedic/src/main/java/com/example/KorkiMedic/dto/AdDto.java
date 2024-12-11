package com.example.KorkiMedic.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdDto {

    private String title;
    private String description;
    private byte[] image; // Obraz przechowywany jako tablica bajtów

}

