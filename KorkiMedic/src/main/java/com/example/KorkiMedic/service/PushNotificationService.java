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

/*import com.example.KorkiMedic.entity.Appointment;
import com.example.KorkiMedic.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PushNotificationService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";
    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(fixedRate = 60000) // Sprawdzaj co minutę
    public void sendAppointmentNotifications() {
        LocalDateTime oneHourFromNow = LocalDateTime.now().plusHours(1);
        LocalDateTime now = LocalDateTime.now();

        // Znajdź wizyty w godzinie przed ich terminem
        List<Appointment> upcomingAppointments = appointmentRepository.findAppointmentsByDateBetween(now, oneHourFromNow);

        for (Appointment appointment : upcomingAppointments) {
            // Przygotuj dane powiadomienia
            String title = "Przypomnienie o wizycie";
            String body = "Masz nadchodzącą wizytę o godzinie " + appointment.getDate().toString() + ".";
            String userToken = appointment.getPatient().getFcmToken();

            Map<String, Object> payload = new HashMap<>();
            payload.put("to", userToken);
            payload.put("title", title);
            payload.put("body", body);
            payload.put("sound", "default");
            restTemplate.postForObject(EXPO_PUSH_URL, payload, String.class);

        }
    }
}*/
/*import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PushNotificationService {

    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserPushTokenRepository userPushTokenRepository;

    public void sendPushNotification(Long userId, String title, String body) {
        userPushTokenRepository.findByUserId(userId).ifPresent(userPushToken -> {
            Map<String, Object> payload = new HashMap<>();
            payload.put("to", userPushToken.getExpoPushToken());
            payload.put("title", title);
            payload.put("body", body);
            payload.put("sound", "default");

            restTemplate.postForObject(EXPO_PUSH_URL, payload, String.class);
        });
    }
}*/

