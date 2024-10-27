package com.example.KorkiMedic.service;

import com.example.KorkiMedic.dto.AppointmentDTO;
import com.example.KorkiMedic.dto.AppointmentRequest;
import com.example.KorkiMedic.entity.Appointment;
import com.example.KorkiMedic.entity.ServReward;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.entity.Serv;
import com.example.KorkiMedic.exceptions.EntityNotFoundException;
import com.example.KorkiMedic.repository.AppointmentRepository;
import com.example.KorkiMedic.repository.ServRewardRepository;
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

    @Autowired
    private ServRewardRepository servRewardRepository;

    public Appointment createAppointment(AppointmentRequest appointmentRequest,String PatientEmail) {
        User patient = userRepository.findByEmail(PatientEmail)
                .orElseThrow(() -> EntityNotFoundException.patientNotFound(PatientEmail));

        User doctor = userRepository.findById(appointmentRequest.getDoctorId())
                .orElseThrow(() -> EntityNotFoundException.doctorNotFound(appointmentRequest.getDoctorId().toString()));

        Serv service = serviceRepository.findById(appointmentRequest.getServiceName())
                .orElseThrow(() -> EntityNotFoundException.serviceNotFound(appointmentRequest.getServiceName().toString()));

        if(appointmentRepository.findByDoctorAndDate(doctor,appointmentRequest.getDate()).isPresent()) {
            throw EntityNotFoundException.dateReservedFound();
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setService(service);
        appointment.setDate(java.sql.Timestamp.valueOf(appointmentRequest.getDate()));
        appointment.setDescription(appointmentRequest.getDescription());
        if(appointmentRequest.getRewardId() != null) {
            ServReward servReward = servRewardRepository.findById(appointmentRequest.getRewardId()).orElse(null);
            assert servReward != null;
            appointment.setPrice(servReward.getServ().getPrice() - servReward.getDiscount());
        }
        return appointmentRepository.save(appointment);
    }
    public void confirmAppointment(Long appointmentId, User doctor) {
        Appointment appointment = appointmentRepository.findByIdAndDoctor(appointmentId,doctor)
                .orElseThrow(EntityNotFoundException::AppointmentNotFoundException);
        appointment.setStatus("Potwierdzona");
        appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long appointmentId, User user) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(EntityNotFoundException::AppointmentNotFoundException);
        appointment.setStatus("Anulowana");
        appointmentRepository.save(appointment);
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
        return mapAppointmentsDoctorToDTO(appointments);
    }

    // Metoda do mapowania Appointment na AppointmentDTO
    private AppointmentDTO mapAppointmentToDTO(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getDoctor().getFirstName(),
                appointment.getDoctor().getLastName(),
                appointment.getService().getName(),
                appointment.getDescription(),
                appointment.getPrice(),
                appointment.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                appointment.getStatus(),
                false
        );
    }

    private AppointmentDTO mapAppointmentDoctorToDTO(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getPatient().getFirstName(),
                appointment.getPatient().getLastName(),
                appointment.getService().getName(),
                appointment.getDescription(),
                appointment.getPrice(),
                appointment.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                appointment.getStatus(),
                true
        );
    }
    // Metoda do mapowania listy Appointment na listę AppointmentDTO
    private List<AppointmentDTO> mapAppointmentsToDTO(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::mapAppointmentToDTO)
                .collect(Collectors.toList());
    }

    private List<AppointmentDTO> mapAppointmentsDoctorToDTO(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::mapAppointmentDoctorToDTO)
                .collect(Collectors.toList());
    }

    public void updateAppointmentDescription(Long appointmentId, String notes, User doctor) {
        // Find appointment by ID
        Appointment appointment = appointmentRepository.findByIdAndDoctor(appointmentId,doctor)
                .orElseThrow(EntityNotFoundException::AppointmentNotFoundException);

        // Update description
        appointment.setDescription(notes);

        // Save the updated appointment
        appointmentRepository.save(appointment);
    }

    public void setStatusAppointment(Long id, User doctor, String status) {
        Appointment appointment = appointmentRepository.findByIdAndDoctor(id,doctor)
                .orElseThrow(EntityNotFoundException::AppointmentNotFoundException);
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
    }
}
