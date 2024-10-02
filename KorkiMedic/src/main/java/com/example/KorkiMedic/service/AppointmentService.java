package com.example.KorkiMedic.service;

import com.example.KorkiMedic.dto.AppointmentDTO;
import com.example.KorkiMedic.dto.AppointmentRequest;
import com.example.KorkiMedic.entity.Appointment;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.entity.Serv;
import com.example.KorkiMedic.exceptions.EntityNotFoundException;
import com.example.KorkiMedic.repository.AppointmentRepository;
import com.example.KorkiMedic.repository.UserRepository;
import com.example.KorkiMedic.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public Appointment createAppointment(AppointmentRequest appointmentRequest,String PatientEmail) {
        User patient = userRepository.findByEmail(PatientEmail)
                .orElseThrow(() -> EntityNotFoundException.patientNotFound(PatientEmail));

        User doctor = userRepository.findById(appointmentRequest.getDoctorId())
                .orElseThrow(() -> EntityNotFoundException.doctorNotFound(appointmentRequest.getDoctorId().toString()));

        Serv service = serviceRepository.findByName(appointmentRequest.getServiceName())
                .orElseThrow(() -> EntityNotFoundException.serviceNotFound(appointmentRequest.getServiceName()));
        Appointment appoin = appointmentRepository.findByDoctorAndDate(doctor,appointmentRequest.getDate())
                .orElseThrow(EntityNotFoundException::dateReservedFound);


        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setService(service);
        appointment.setDate(java.sql.Timestamp.valueOf(appointmentRequest.getDate()));
        appointment.setDescription(appointmentRequest.getDescription());

        return appointmentRepository.save(appointment);
    }

    public List<AppointmentDTO> getAppointmentsByPatient(String patientEmail) {
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> EntityNotFoundException.patientNotFound(patientEmail));

        List<Appointment> appointments = appointmentRepository.findByPatient(patient);
        return mapAppointmentsToDTO(appointments);
    }

    // Metoda do pobierania wizyt lekarza
    public List<AppointmentDTO> getAppointmentsByDoctor(String doctorEmail) {
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> EntityNotFoundException.doctorNotFound(doctorEmail));
        List<Appointment> appointments = appointmentRepository.findByDoctor(doctor);
        return mapAppointmentsToDTO(appointments);
    }

    // Metoda do mapowania Appointment na AppointmentDTO
    private AppointmentDTO mapAppointmentToDTO(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getPatient().getFirstName(),
                appointment.getPatient().getLastName(),
                appointment.getDoctor().getFirstName(),
                appointment.getDoctor().getLastName(),
                appointment.getService(), // Cały obiekt Serv
                appointment.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                "confirmed" // Przykładowy status, może być dynamiczny
        );
    }

    // Metoda do mapowania listy Appointment na listę AppointmentDTO
    private List<AppointmentDTO> mapAppointmentsToDTO(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::mapAppointmentToDTO)
                .collect(Collectors.toList());
    }
}
