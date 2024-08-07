package com.example.KorkiMedic.init;

import com.example.KorkiMedic.entity.*;
import com.example.KorkiMedic.enums.Role;
import com.example.KorkiMedic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RewardRepository rewardRepository;
    private final UserRepository userRepository;
    private final PointActionRepository pointActionRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpecializationRepository specializationRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public DataInitializer(RewardRepository rewardRepository, UserRepository userRepository, PointActionRepository pointActionRepository, PasswordEncoder passwordEncoder, SpecializationRepository specializationRepository, ServiceRepository serviceRepository, AppointmentRepository appointmentRepository) {
        this.rewardRepository = rewardRepository;
        this.userRepository = userRepository;
        this.pointActionRepository = pointActionRepository;
        this.passwordEncoder = passwordEncoder;
        this.specializationRepository = specializationRepository;
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Adding sample rewards
            rewardRepository.save(new Reward("Bon Zniżkowy", "10% zniżki na następną wizytę", 50));
            rewardRepository.save(new Reward("Bezpłatna Wizyta Kontrolna", "Bezpłatna sesja kontrolna", 100));
            rewardRepository.save(new Reward("Pakiet Zdrowotny", "Kompleksowy pakiet badań zdrowotnych", 200));
            rewardRepository.save(new Reward("Konsultacja Dietetyczna", "Bezpłatna konsultacja z dietetykiem", 70));
            rewardRepository.save(new Reward("Masaż Relaksacyjny", "Bezpłatny masaż relaksacyjny", 120));

            // Roles
            Set<Role> adminRole = new HashSet<>();
            adminRole.add(Role.ADMIN);

            Set<Role> userRole = new HashSet<>();
            userRole.add(Role.USER);

            Set<Role> multipleRoles = new HashSet<>();
            multipleRoles.add(Role.USER);
            multipleRoles.add(Role.ADMIN);

            // Creating example users
            User user1 = new User(null, "Jan", "Kowalski", "jan@example.com", "123456789", LocalDate.of(1985, 5, 15), 0,
                    userRole, passwordEncoder.encode("qwerty"));
            User user2 = new User(null, "Anna", "Nowak", "anna.nowak@example.com", "987654321", LocalDate.of(1990, 8, 20), 0,
                    adminRole, passwordEncoder.encode("qwerty"));
            User user3 = new User(null, "Piotr", "Wiśniewski", "piotr.wisniewski@example.com", "555555555", LocalDate.of(1975, 12, 25), 0,
                    multipleRoles, passwordEncoder.encode("qwerty"));

            userRepository.saveAll(List.of(user1, user2, user3));

            // Specializations
            Specialization cardiology = new Specialization(null, "Cardiology");
            Specialization neurology = new Specialization(null, "Neurology");
            Specialization orthopedics = new Specialization(null, "Orthopedics");

            specializationRepository.saveAll(Set.of(cardiology, neurology, orthopedics));

            // Services
            Service consultation = new Service(null, "Consultation", "General medical consultation", 200);
            Service surgery = new Service(null, "Surgery", "Minor surgical procedures", 1500);
            Service therapy = new Service(null, "Therapy", "Physical therapy sessions", 300);

            serviceRepository.saveAll(Set.of(consultation, surgery, therapy));

            // Creating doctors with specializations and services
            User doctor1 = new User(null, "John", "Doe", "john@example.com", "123456788", LocalDate.of(1980, 5, 15), 0,
                    Set.of(Role.DOCTOR), Set.of(cardiology, neurology), Set.of(consultation, therapy), null, null, passwordEncoder.encode("qwerty"));
            User doctor2 = new User(null, "Jane", "Smith", "jane.smith@example.com", "987654389", LocalDate.of(1975, 3, 25), 0,
                    Set.of(Role.DOCTOR), Set.of(orthopedics), Set.of(surgery, therapy), null, null, passwordEncoder.encode("qwerty"));

            userRepository.saveAll(List.of(doctor1, doctor2));
        }

        if (appointmentRepository.count() == 0) {
            User patient = userRepository.findByEmail("jan@example.com").orElseThrow(() -> new RuntimeException("User not found"));
            User doctor1 = userRepository.findByEmail("john@example.com").orElseThrow(() -> new RuntimeException("Doctor not found"));
            User doctor2 = userRepository.findByEmail("jane.smith@example.com").orElseThrow(() -> new RuntimeException("Doctor not found"));

            Service consultation = serviceRepository.findByName("Consultation").orElseThrow(() -> new RuntimeException("Service not found"));
            Service surgery = serviceRepository.findByName("Surgery").orElseThrow(() -> new RuntimeException("Service not found"));

            // Utwórz obiekt Calendar ustawiony na dzisiejszą datę
            Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.set(Calendar.HOUR, 16);
            calendar.set(Calendar.MINUTE, 15);
            calendar.set(Calendar.SECOND, 0);

            Date tomorrowDate = calendar.getTime();

            Appointment appointment1 = new Appointment(null, patient, doctor1, consultation, tomorrowDate, "Routine checkup", null, null);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.set(Calendar.HOUR, 13);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            tomorrowDate = calendar.getTime();
            Appointment appointment2 = new Appointment(null, patient, doctor2, surgery, tomorrowDate, "Scheduled surgery", null, null);
            appointmentRepository.saveAll(List.of(appointment1, appointment2));
        }

        if (pointActionRepository.count() == 0) {
            // Adding default point actions
            List<PointAction> defaultActions = List.of(
                    new PointAction(null, 10, "Za rejestrację w systemie", LocalDateTime.now(), LocalDateTime.now()),
                    new PointAction(null, 5, "Za zapisanie się na wizytę", LocalDateTime.now(), LocalDateTime.now()),
                    new PointAction(null, 20, "Za polecenie nowego pacjenta", LocalDateTime.now(), LocalDateTime.now()),
                    new PointAction(null, -15, "Za wymianę punktów na nagrodę", LocalDateTime.now(), LocalDateTime.now()),
                    new PointAction(null, 5, "Codzienne logowanie", LocalDateTime.now(), LocalDateTime.now())
            );

            pointActionRepository.saveAll(defaultActions);
            System.out.println("Dodano domyślne akcje punktowe.");
        } else {
            System.out.println("Akcje punktowe już istnieją.");
        }
    }
}
