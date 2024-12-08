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
    private final AppointmentRepository appointmentRepository;

    private final ServRewardRepository servRewardRepository;

    private final ServiceRepository servRepository;
    @Autowired
    public DataInitializer(RewardRepository rewardRepository, UserRepository userRepository, PointActionRepository pointActionRepository, PasswordEncoder passwordEncoder, SpecializationRepository specializationRepository, ServiceRepository serviceRepository, AppointmentRepository appointmentRepository, ServRewardRepository servRewardRepository, ServiceRepository servRepository) {
        this.rewardRepository = rewardRepository;
        this.userRepository = userRepository;
        this.pointActionRepository = pointActionRepository;
        this.passwordEncoder = passwordEncoder;
        this.specializationRepository = specializationRepository;
        this.appointmentRepository = appointmentRepository;
        this.servRewardRepository = servRewardRepository;
        this.servRepository = servRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Adding sample rewards
            rewardRepository.save(new Reward("Bon Zniżkowy", "50 zł zniżki", 50));
            rewardRepository.save(new Reward("Bezpłatna Wizyta Kontrolna", "Bezpłatna sesja kontrolna", 0));
            rewardRepository.save(new Reward("Pakiet Zdrowotny", "Kompleksowy pakiet badań zdrowotnych", 0));
            rewardRepository.save(new Reward("Konsultacja Dietetyczna", "Bezpłatna konsultacja z dietetykiem", 0));
            rewardRepository.save(new Reward("Masaż Relaksacyjny", "Bezpłatny masaż relaksacyjny", 0));

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
            Serv service1 = new Serv();
            service1.setName("Konsultacja Lekarska");
            service1.setDescription("Konsultacja z lekarzem specjalistą.");
            service1.setPrice(200);

            Serv service2 = new Serv();
            service2.setName("Badanie krwi");
            service2.setDescription("Kompleksowe badanie krwi.");
            service2.setPrice(100);

            Serv service3 = new Serv();
            service3.setName("USG");
            service3.setDescription("USG brzucha.");
            service3.setPrice(150);

            servRepository.save(service1);
            servRepository.save(service2);
            servRepository.save(service3);

            // Tworzenie przykładowych nagród
            Reward reward1 = new Reward();
            reward1.setName("Zniżka 10%");
            reward1.setDescription("Zniżka 10% na wybraną usługę.");
            reward1.setDiscount(0.1F);

            Reward reward2 = new Reward();
            reward2.setName("Zniżka 20%");
            reward2.setDescription("Zniżka 20% na wybraną usługę.");
            reward2.setDiscount(0.2F);

            Reward reward3 = new Reward();
            reward3.setName("Bezpłatne badanie krwi");
            reward3.setDescription("Bezpłatne wykonanie badania krwi.");
            reward3.setDiscount(0.0F);

            rewardRepository.save(reward1);
            rewardRepository.save(reward2);
            rewardRepository.save(reward3);

            // Tworzenie powiązań między usługami a nagrodami z obniżką
            ServReward servReward1 = new ServReward(service1, reward1, 10);
            ServReward servReward2 = new ServReward(service2, reward2, 20);
            ServReward servReward3 = new ServReward(service3, reward3, 100);

            servRewardRepository.save(servReward1);
            servRewardRepository.save(servReward2);
            servRewardRepository.save(servReward3);

            // Creating doctors with specializations and services
            User doctor1 = new User(null, "John", "Doe", "john@example.com", "123456788", LocalDate.of(1980, 5, 15), 0,
                    Set.of(Role.DOCTOR,Role.USER), Set.of(cardiology, neurology), Set.of(service1, service2),null, null, null, passwordEncoder.encode("qwerty"));
            User doctor2 = new User(null, "Jane", "Smith", "jane.smith@example.com", "987654389", LocalDate.of(1975, 3, 25), 0,
                    Set.of(Role.DOCTOR,Role.USER), Set.of(orthopedics), Set.of(service1, service3),null, null, null, passwordEncoder.encode("qwerty"));

            userRepository.saveAll(List.of(doctor1, doctor2));

            if (appointmentRepository.count() == 0) {
                User patient = userRepository.findByEmail("jan@example.com").orElseThrow(() -> new RuntimeException("User not found"));

                // Utwórz obiekt Calendar ustawiony na dzisiejszą datę
                Calendar calendar = Calendar.getInstance();

                calendar.add(Calendar.DAY_OF_YEAR, 1);
                calendar.set(Calendar.HOUR, 16);
                calendar.set(Calendar.MINUTE, 15);
                calendar.set(Calendar.SECOND, 0);

                Date tomorrowDate = calendar.getTime();

                Appointment appointment1 = new Appointment(null, patient, doctor1, service1, tomorrowDate,null,100, "Brak notatki", "Zatwierdzona", null, null);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                calendar.set(Calendar.HOUR, 13);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                tomorrowDate = calendar.getTime();
                Appointment appointment2 = new Appointment(null, patient, doctor2, service2, tomorrowDate,null, 550, "Brak notatki", "Anulowana", null, null);
                appointmentRepository.saveAll(List.of(appointment1, appointment2));
                adminRole = new HashSet<>();
                adminRole.add(Role.ADMIN);

               userRole = new HashSet<>();
                userRole.add(Role.USER);

                multipleRoles = new HashSet<>();
                multipleRoles.add(Role.USER);
                multipleRoles.add(Role.ADMIN);

                // Tworzenie dodatkowych użytkowników
                User user4 = new User(null, "Katarzyna", "Wiśniewska", "katarzyna.wisniewska@example.com", "777777777", LocalDate.of(1995, 1, 10), 0,
                        userRole, passwordEncoder.encode("haslo123"));
                User user5 = new User(null, "Marek", "Zieliński", "marek.zielinski@example.com", "444444444", LocalDate.of(1980, 11, 5), 0,
                        adminRole, passwordEncoder.encode("haslo456"));
                User user6 = new User(null, "Paulina", "Dąbrowska", "paulina.dabrowska@example.com", "666666666", LocalDate.of(1992, 6, 18), 0,
                        multipleRoles, passwordEncoder.encode("haslo789"));

                userRepository.saveAll(List.of(user4, user5, user6));

                // Dodatkowe specjalizacje
                Specialization dermatology = new Specialization(null, "Dermatologia");
                Specialization pediatrics = new Specialization(null, "Pediatria");
                Specialization psychiatry = new Specialization(null, "Psychiatria");

                specializationRepository.saveAll(Set.of(dermatology, pediatrics, psychiatry));

                // Dodatkowe usługi
                Serv service4 = new Serv();
                service4.setName("Konsultacja Dermatologiczna");
                service4.setDescription("Konsultacja z dermatologiem.");
                service4.setPrice(180);

                Serv service5 = new Serv();
                service5.setName("Terapia Psychologiczna");
                service5.setDescription("Indywidualna sesja terapeutyczna.");
                service5.setPrice(220);

                Serv service6 = new Serv();
                service6.setName("Rehabilitacja");
                service6.setDescription("Zabieg rehabilitacyjny.");
                service6.setPrice(250);

                servRepository.save(service4);
                servRepository.save(service5);
                servRepository.save(service6);

                // Nowe nagrody
                Reward reward4 = new Reward();
                reward4.setName("Konsultacja dietetyczna");
                reward4.setDescription("Bezpłatna konsultacja dietetyczna.");
                reward4.setDiscount(0);

                Reward reward5 = new Reward();
                reward5.setName("Bezpłatne USG");
                reward5.setDescription("Bezpłatne wykonanie USG.");
                reward5.setDiscount(0);

                rewardRepository.save(reward4);
                rewardRepository.save(reward5);

                // Nowe powiązania między usługami a nagrodami z obniżką
                ServReward servReward4 = new ServReward(service4, reward4, 50); // 50% zniżki na Konsultację Dermatologiczną
                ServReward servReward5 = new ServReward(service6, reward5, 100); // 100% zniżki (darmowe) na Rehabilitację

                servRewardRepository.save(servReward4);
                servRewardRepository.save(servReward5);

                // Dodanie nowych lekarzy ze specjalizacjami i usługami
                User doctor3 = new User(null, "Agnieszka", "Kwiatkowska", "agnieszka.kwiatkowska@example.com", "888888888", LocalDate.of(1982, 4, 5), 0,
                        Set.of(Role.DOCTOR,Role.USER), Set.of(dermatology), Set.of(service4),null, null, null, passwordEncoder.encode("haslo123"));
                User doctor4 = new User(null, "Tomasz", "Lewandowski", "tomasz.lewandowski@example.com", "999999999", LocalDate.of(1978, 9, 20), 0,
                        Set.of(Role.DOCTOR,Role.USER), Set.of(pediatrics, psychiatry), Set.of(service5),null, null, null, passwordEncoder.encode("haslo123"));

                userRepository.saveAll(List.of(doctor3, doctor4));

                // Tworzenie przykładowych wizyt
                patient = userRepository.findByEmail("jan@example.com").orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

                calendar = Calendar.getInstance();

                // Wizyta za dwa dni
                calendar.add(Calendar.DAY_OF_YEAR, 2);
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 30);
                calendar.set(Calendar.SECOND, 0);

                Date futureDate = calendar.getTime();

                Appointment appointment3 = new Appointment(null, patient, doctor3, service4, futureDate,null,300, "Konsultacja dermatologiczna","Niezatwierdzony", null, null);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                futureDate = calendar.getTime();
                Appointment appointment4 = new Appointment(null, patient, doctor4, service5, futureDate,null,200, "Terapia psychologiczna", "Anulowany" ,null, null);
                appointmentRepository.saveAll(List.of(appointment3, appointment4));
            }

            if (pointActionRepository.count() == 0) {
                // Adding default point actions
                List<PointAction> defaultActions = List.of(
                        new PointAction(null, "Za rejestrację w systemie", LocalDateTime.now(), LocalDateTime.now()),
                        new PointAction(null,  "Za realizacje wizyty", LocalDateTime.now(), LocalDateTime.now()),
                        new PointAction(null,  "Zwrot punktów", LocalDateTime.now(), LocalDateTime.now()),
                        new PointAction(null,  "Za polecenie nowego pacjenta", LocalDateTime.now(), LocalDateTime.now()),
                        new PointAction(null,  "Za wymianę punktów na nagrodę", LocalDateTime.now(), LocalDateTime.now()),
                        new PointAction(null, "Codzienne logowanie", LocalDateTime.now(), LocalDateTime.now())
                );

                pointActionRepository.saveAll(defaultActions);
                System.out.println("Dodano domyślne akcje punktowe.");
            } else {
                System.out.println("Akcje punktowe już istnieją.");
            }
        }


    }
}
