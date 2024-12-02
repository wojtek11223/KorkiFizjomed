package com.example.KorkiMedic.controllers;

import com.example.KorkiMedic.dto.DoctorAppointmentsDTO;
import com.example.KorkiMedic.dto.DoctorInfoDTO;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.exceptions.EntityNotFoundException;
import com.example.KorkiMedic.service.DoctorService;
import com.example.KorkiMedic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    @GetMapping("/{doctorId}/appointments")
    public ResponseEntity<?> getDoctorAppointments(@PathVariable Long doctorId) {

        if (!userService.isUserDoctor(doctorId)) {
            throw EntityNotFoundException.doctorNotFound(doctorId.toString());
        }
        User doctor = userService.findUserById(doctorId);


        DoctorAppointmentsDTO doctorAppointments = doctorService.getDoctorAppointments(doctor);
        return ResponseEntity.ok(doctorAppointments);
    }

    @GetMapping("/info")
    public ResponseEntity<List<DoctorInfoDTO>> getDoctorsInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        List<DoctorInfoDTO> doctorsInfo = doctorService.getAllDoctorsWithSpecializations(currentUser.getId());
        return ResponseEntity.ok(doctorsInfo);
    }
}
