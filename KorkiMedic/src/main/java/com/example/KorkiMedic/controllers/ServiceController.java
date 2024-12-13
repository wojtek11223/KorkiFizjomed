package com.example.KorkiMedic.controllers;

import com.example.KorkiMedic.dto.ServiceIdsDTO;
import com.example.KorkiMedic.dto.ServiceResponseDTO;
import com.example.KorkiMedic.entity.Serv;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.service.ServService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServService serviceService;

    public ServiceController(ServService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceResponseDTO>> getAllServicesWithDoctorServices() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User doctor = (User) authentication.getPrincipal();
        return ResponseEntity.ok(serviceService.getAllServicesWithDoctorServices(doctor));
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateDoctorServices(@RequestBody ServiceIdsDTO serviceIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User doctor = (User) authentication.getPrincipal();
        serviceService.updateDoctorServices(serviceIds.getServiceId(),doctor);
        return ResponseEntity.ok("Zaktualizowano twoje usługi");
    }
}
