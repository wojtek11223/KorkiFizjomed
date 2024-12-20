package com.example.KorkiMedic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "serv_id", nullable = false)
    private Serv serv;

    @ManyToOne
    @JoinColumn(name = "reward_id", nullable = false)
    private Reward reward;
    private Integer pointsRequired;
    // Dodatkowe pole do przechowywania obniżki ceny
     // wartość obniżki ceny w procentach lub inna jednostka

    public ServReward(Serv serv, Reward reward, Integer pointsRequired) {
        this.serv = serv;
        this.reward = reward;
        this.pointsRequired = pointsRequired;
    }
}
