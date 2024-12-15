package com.example.KorkiMedic.service;

import com.example.KorkiMedic.dto.DoctorAppointmentsDTO;
import com.example.KorkiMedic.dto.DoctorInfoDTO;
import com.example.KorkiMedic.dto.ServiceDTO;
import com.example.KorkiMedic.entity.Appointment;
import com.example.KorkiMedic.entity.Serv;
import com.example.KorkiMedic.entity.Specialization;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.enums.Role;
import com.example.KorkiMedic.repository.AppointmentRepository;
import com.example.KorkiMedic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;

    public DoctorAppointmentsDTO getDoctorAppointments(User doctor) {
        List<Appointment> appointments = appointmentRepository.findByDoctor(doctor);

        // Mapowanie zarezerwowanych terminów na DTO
        List<DoctorAppointmentsDTO.AppointmentInfo> appointmentInfos = appointments.stream()
                .filter(appointment -> appointment.getDate().toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime()
                        .isAfter(LocalDateTime.now())) // Filter future appointments
                .map(appointment -> new DoctorAppointmentsDTO.AppointmentInfo(
                        appointment.getDate().toInstant()
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDateTime(),
                        appointment.getDescription()))
                .collect(Collectors.toList());

        // Mapowanie specjalizacji i usług
        List<String> specializations = doctor.getSpecializations().stream()
                .map(Specialization::getName)
                .collect(Collectors.toList());

        List<ServiceDTO> services = appointments.stream()
                .map(appointment -> new ServiceDTO(
                        appointment.getService().getId(),
                        appointment.getService().getName(),
                        appointment.getService().getPrice()))
                .collect(Collectors.toList());


        return new DoctorAppointmentsDTO(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                specializations,
                services,
                appointmentInfos
        );
    }
    public List<DoctorInfoDTO> getAllDoctorsWithSpecializations(Long currentUserId) {
        return userRepository.findAllByRoleAndNotCurrentUser(Role.DOCTOR,currentUserId).stream()
                .map(user -> new DoctorInfoDTO(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getSpecializations().stream()
                                .map(Specialization::getName)
                                .collect(Collectors.toSet()),
                        user.getServices().stream()
                                .map(serv -> new ServiceDTO(
                                        serv.getId(),
                                        serv.getName(),
                                        serv.getPrice()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
