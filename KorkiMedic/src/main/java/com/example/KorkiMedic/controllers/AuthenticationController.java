package com.example.KorkiMedic.controllers;

import com.example.KorkiMedic.dto.LoginResponse;
import com.example.KorkiMedic.dto.LoginUserDto;
import com.example.KorkiMedic.dto.RegisterUserDto;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.enums.Role;
import com.example.KorkiMedic.service.AuthenticationService;
import com.example.KorkiMedic.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@CrossOrigin("http://192.168.0.101:8081")
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        authenticationService.addDailyLoginPoints(authenticatedUser);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken,jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }
}