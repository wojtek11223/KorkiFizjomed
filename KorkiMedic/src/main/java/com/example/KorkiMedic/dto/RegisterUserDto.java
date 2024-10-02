package com.example.KorkiMedic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterUserDto {

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private LocalDate dateOfBirth;

}