package com.example.KorkiMedic.repository;

import com.example.KorkiMedic.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
