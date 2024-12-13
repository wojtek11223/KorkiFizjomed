package com.example.KorkiMedic.controllers;

import com.example.KorkiMedic.dto.UserPointActionDTO;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.service.UserPointActionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-point-actions")
public class UserPointActionController {

    private final UserPointActionService userPointActionService;

    public UserPointActionController(UserPointActionService userPointActionService) {
        this.userPointActionService = userPointActionService;
    }

    @GetMapping
    public List<UserPointActionDTO> getUserPointActions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return userPointActionService.getUserPointActions(currentUser);
    }
}
