package com.example.KorkiMedic.controllers;
import com.example.KorkiMedic.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class PushNotificationController {

    @Autowired
    private PushNotificationService pushNotificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(
            @RequestParam String expoPushToken,
            @RequestParam String title,
            @RequestParam String message) {

        String response = pushNotificationService.sendPushNotification(expoPushToken, title, message);
        return ResponseEntity.ok(response);
    }
}
