package com.example.KorkiMedic.service;

import com.example.KorkiMedic.dto.AppointmentDTO;
import com.example.KorkiMedic.dto.AppointmentRequest;
import com.example.KorkiMedic.dto.StatusDTO;
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
import java.util.Objects;
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

    @Autowired
    private PushNotificationService pushNotificationService;
    @Autowired
    private UserService userService;

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
            if(appointment.getPatient().getLoyaltyPoints() - servReward.getDiscount() < 0)
            {
                throw EntityNotFoundException.loyalityPointsException();
            }

            appointment.setPrice(servReward.getServ().getPrice() - servReward.getDiscount());
            appointment.setServReward(servReward);
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

    public void setStatusAppointment(Long id, User user, StatusDTO statusDTO) {
        Appointment appointment;
        if (!"Anulowana".equals(statusDTO.getStatus()) &&
                !"Potwierdzona".equals(statusDTO.getStatus()) &&
                !"Zrealizowana".equals(statusDTO.getStatus())) {
           throw EntityNotFoundException.SetStatusException();
        }
        if(statusDTO.isDoctor())
        {
            appointment = appointmentRepository.findByIdAndDoctor(id,user)
                    .orElseThrow(EntityNotFoundException::AppointmentNotFoundException);
        } else {
            appointment = appointmentRepository.findByIdAndPatient(id,user)
                    .orElseThrow(EntityNotFoundException::AppointmentNotFoundException);
        }
        if(Objects.equals(appointment.getStatus(), "Zrealizowana") ||
                Objects.equals(appointment.getStatus(), "Anulowana")) {
            throw EntityNotFoundException.SetStatusException();
        }
        appointment.setStatus(statusDTO.getStatus());
        if(Objects.equals(statusDTO.getStatus(), "Zrealizowana")) {
            userService.addLoyalityPoints(appointment.getPatient(),appointment.getService().getPrice());
            userService.addLoyalityPoints(appointment.getDoctor(),appointment.getService().getPrice()/2);
        }
        if(Objects.equals(statusDTO.getStatus(), "Anulowana"))  {
            userService.addLoyalityPoints(appointment.getPatient(),appointment.getServReward().getDiscount());
        }
        appointmentRepository.save(appointment);
        pushNotificationService.sendPushNotification(appointment.getPatient().getFcmToken(),"Zmiana statusu wizyty", "Wizyta dnia " + appointment.getDate().toString() + "zmieniła status wizyty");

    }
}
