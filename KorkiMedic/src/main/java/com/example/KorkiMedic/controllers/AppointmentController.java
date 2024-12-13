package com.example.KorkiMedic.controllers;

import com.example.KorkiMedic.dto.*;
import com.example.KorkiMedic.entity.Appointment;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<String> createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Appointment appointment = appointmentService.createAppointment(appointmentRequest, username);
        return ResponseEntity.ok("Udało się zarejestrować");
    }
    @GetMapping("/patient")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<AppointmentDTO> appointmentDTOs = appointmentService.getAppointmentsByPatient(userDetails.getUsername());
        return ResponseEntity.ok(appointmentDTOs);
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<AppointmentDTO> appointmentDTOs = appointmentService.getAppointmentsByDoctor(userDetails.getUsername());
        return ResponseEntity.ok(appointmentDTOs);
    }

    @PostMapping("/{id}/setStatus")
    public ResponseEntity<?> confirmAppointment(@PathVariable Long id,
                                                @RequestBody StatusDTO statusDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) authentication.getPrincipal();
        appointmentService.setStatusAppointment(id,user,statusDTO);
        return ResponseEntity.ok("Wizyta zmieniła status");
    }

    @PostMapping("/{appointmentId}/add-notes")
    public ResponseEntity<String> addDoctorNotes(@PathVariable Long appointmentId,
                                                 @RequestBody NotesDTO notes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User doctor = (User) authentication.getPrincipal(); // Get logged in doctor
        appointmentService.updateAppointmentDescription(appointmentId, notes.getNotes(), doctor);
        return ResponseEntity.ok("Notes added successfully");
    }
    @PostMapping("/{appointmentId}/info")
    public ResponseEntity<AppointmentDetailsDTO> getAppointmentInfo(
            @PathVariable Long appointmentId,
            @RequestBody whoCallDTO requestBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) authentication.getPrincipal();
        AppointmentDetailsDTO appointmentDetailsDTO = appointmentService.getAppointmentDetails(user, requestBody.getIsDoctor(), appointmentId);
        return ResponseEntity.ok(appointmentDetailsDTO);

    }

}