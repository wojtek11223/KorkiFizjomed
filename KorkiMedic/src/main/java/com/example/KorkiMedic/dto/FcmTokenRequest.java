package com.example.KorkiMedic.dto;

import lombok.Getter;

@Getter
public class FcmTokenRequest {
    private String fcmToken;

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}