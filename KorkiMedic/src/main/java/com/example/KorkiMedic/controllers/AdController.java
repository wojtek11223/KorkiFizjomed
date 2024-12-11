package com.example.KorkiMedic.controllers;

import com.example.KorkiMedic.dto.AdDto;
import com.example.KorkiMedic.repository.AdRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ads")
public class AdController {

    private final AdRepository adRepository;

    public AdController(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    @GetMapping("top")
    public List<AdDto> getLatestAds() {
        // Mapowanie encji na DTO
        return adRepository.findTop3ByOrderByCreatedAtDesc()
                .stream()
                .map(ad -> new AdDto(ad.getTitle(), ad.getDescription(), ad.getImage()))
                .collect(Collectors.toList());
    }
}
