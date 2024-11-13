package com.example.KorkiMedic.service;

import com.example.KorkiMedic.entity.Appointment;
import com.example.KorkiMedic.repository.AppointmentRepository;
import com.example.KorkiMedic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Scheduled(fixedRate = 60000)
    public void sendAppointmentNotifications() {
        LocalDateTime oneHourFromNowMinusMinutes = LocalDateTime.now().plusHours(1).minusSeconds(29);
        LocalDateTime oneHourFromNowPlusMinutes = LocalDateTime.now().plusHours(1).plusSeconds(31);

        List<Appointment> upcomingAppointments = appointmentRepository.findAppointmentsByDateBetween(oneHourFromNowMinusMinutes,oneHourFromNowPlusMinutes);

        for (Appointment appointment : upcomingAppointments) {
            String fcmToken = appointment.getPatient().getFcmToken();
            if (fcmToken != null && !fcmToken.isEmpty() && !Objects.equals(appointment.getStatus(), "Anulowany")) {
                String message = "Przypomnienie: Wizyta o godzinie " + appointment.getDate().toString();
                pushNotificationService.sendPushNotification(fcmToken, "Przypomnienie o wizycie", message);
            }
        }
    }
}
