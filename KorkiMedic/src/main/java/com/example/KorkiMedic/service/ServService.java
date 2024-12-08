package com.example.KorkiMedic.service;

import com.example.KorkiMedic.dto.ServiceResponseDTO;
import com.example.KorkiMedic.entity.Serv;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.repository.ServRepository;
import com.example.KorkiMedic.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServService {

    private final ServRepository servRepository;
    private final UserRepository userRepository;

    public ServService(ServRepository servRepository, UserRepository userRepository) {
        this.servRepository = servRepository;
        this.userRepository = userRepository;
    }

    public List<ServiceResponseDTO> getAllServicesWithDoctorServices(User doctor) {

        List<Serv> allServices = servRepository.findAll();
        Set<Long> doctorServiceIds = doctor.getServices().stream().map(Serv::getId).collect(Collectors.toSet());

        return allServices.stream()
                .map(service -> new ServiceResponseDTO(
                        service.getId(),
                        service.getName(),
                        service.getDescription(),
                        doctorServiceIds.contains(service.getId())
                ))
                .collect(Collectors.toList());
    }

    public void updateDoctorServices(List<Long> serviceIds, User doctor) {

        List<Serv> selectedServices = servRepository.findAllById(serviceIds);
        doctor.setServices(Set.copyOf(selectedServices));
        userRepository.save(doctor);

    }
}
