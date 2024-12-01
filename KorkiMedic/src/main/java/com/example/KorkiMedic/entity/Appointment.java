package com.example.KorkiMedic.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"patient_id", "doctor_id", "date"})
})
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Serv service;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "serv_reward_id", nullable = true)
    private ServReward servReward;

    @Column(nullable = false)
    private float price = 0;

    @Column(length = 500)
    private String description = "";

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        this.status = "Niezatwierdzona";
        if (this.servReward != null) {
            int currentPoints = this.getPatient().getLoyaltyPoints();
            this.getPatient().setLoyaltyPoints(currentPoints - servReward.getPointsRequired());
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
