package com.example.KorkiMedic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KorkiMedicApplication {

	public static void main(String[] args) {
		SpringApplication.run(KorkiMedicApplication.class, args);
	}

}
