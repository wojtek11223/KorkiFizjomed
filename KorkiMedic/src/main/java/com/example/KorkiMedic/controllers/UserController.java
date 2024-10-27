package com.example.KorkiMedic.controllers;

import com.example.KorkiMedic.dto.FcmTokenRequest;
import com.example.KorkiMedic.dto.UserInfoDto;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.service.PushNotificationService;
import com.example.KorkiMedic.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@CrossOrigin("http://192.168.0.101:8081")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new UserInfoDto(
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getPhoneNumber(),
                currentUser.getDateOfBirth(),
                currentUser.getEmail(),
                currentUser.getLoyaltyPoints(),
                userService.isUserDoctor(currentUser.getId())
        ));
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @PostMapping("fcm-token")
    public ResponseEntity<String> updateFcmToken(@RequestBody FcmTokenRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        userService.updateFcmToken(currentUser.getId(), request.getFcmToken());
        return ResponseEntity.ok("Token FCM został zaktualizowany.");
    }
}