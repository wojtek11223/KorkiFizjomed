package com.example.KorkiMedic.exceptions;

import java.time.LocalDateTime;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    // Static factory methods for creating specific exceptions
    public static EntityNotFoundException patientNotFound(String patientId) {
        return new EntityNotFoundException("Patient not found with ID: " + patientId);
    }

    public static EntityNotFoundException doctorNotFound(String doctorId) {
        return new EntityNotFoundException("Doctor not found with ID: " + doctorId);
    }

    public static EntityNotFoundException serviceNotFound(String serviceId) {
        return new EntityNotFoundException("Service not found with ID: " + serviceId);
    }

    public static EntityNotFoundException dateReservedFound() {
        return new EntityNotFoundException("Wybrany termin jest niedostępny");
    }

    public static EntityNotFoundException AppointmentNotFoundException() {
        return new EntityNotFoundException("Wybrany termin jest niedostępny");
    }

    public static EntityNotFoundException AppointmentNotDoctorException() {
        return new EntityNotFoundException("Wybrany wizyta nie należy do lekarza");
    }
}
