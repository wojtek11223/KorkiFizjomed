package com.example.KorkiMedic.repository;

import com.example.KorkiMedic.entity.Appointment;
import com.example.KorkiMedic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctor(User doctor);
    Optional<Appointment> findByDoctorAndDate(User doctor, LocalDateTime date);
    List<Appointment> findByPatient(User patient);
    Optional<Appointment> findByIdAndDoctor(Long appointmentId, User doctor);
}
