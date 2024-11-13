package com.example.KorkiMedic.service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class PushNotificationService {

    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";
    private final RestTemplate restTemplate = new RestTemplate();

    public String sendPushNotification(String expoPushToken, String title, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, Object> notificationPayload = new HashMap<>();
        notificationPayload.put("to", expoPushToken);
        notificationPayload.put("sound", "default");
        notificationPayload.put("title", title);
        notificationPayload.put("body", message);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                EXPO_PUSH_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        return response.getBody();
    }
}