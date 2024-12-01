package com.example.KorkiMedic.exceptions;

import java.time.LocalDateTime;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    // Static factory methods for creating specific exceptions
    public static EntityNotFoundException patientNotFound(String patientId) {
        return new EntityNotFoundException("Nie znaleziono pacjenta o id: " + patientId);
    }

    public static EntityNotFoundException doctorNotFound(String doctorId) {
        return new EntityNotFoundException("Nie znaleziono doktora o id: " + doctorId);
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

    public static EntityNotFoundException PhoneNumberIsUsedException() {
        return new EntityNotFoundException("Istnieje użytkownik z podanym numerem telefonu");
    }

    public static EntityNotFoundException EmailIsUsedException() {
        return new EntityNotFoundException("Istnieje użytkownik z podanym adresem Email");
    }

    public static EntityNotFoundException IllegalPasswordException() {
        return new EntityNotFoundException("Hasło musi posiadać conajmniej 8 znaków");
    }

    public static EntityNotFoundException NotCorrectPasswordException() {
        return new EntityNotFoundException("Stare hasło nie jest poprawne");
    }

    public static EntityNotFoundException loyalityPointsException() {
        return new EntityNotFoundException("Masz za mało punktów do wybrania nagrody");
    }

    public static EntityNotFoundException SetStatusException() {
        return new EntityNotFoundException("Nielegalny status");
    }

    public static EntityNotFoundException NotRewardException() {
        return new EntityNotFoundException("Nie znaleziono nagrody dla wybranej usługi");
    }
}
