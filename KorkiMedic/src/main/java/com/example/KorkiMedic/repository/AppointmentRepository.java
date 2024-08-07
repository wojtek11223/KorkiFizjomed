package com.example.KorkiMedic.repository;

import com.example.KorkiMedic.entity.Appointment;
import com.example.KorkiMedic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctor(User doctor);

}
