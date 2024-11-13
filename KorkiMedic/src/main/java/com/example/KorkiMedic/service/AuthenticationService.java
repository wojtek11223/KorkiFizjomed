package com.example.KorkiMedic.service;

import com.example.KorkiMedic.dto.LoginUserDto;
import com.example.KorkiMedic.dto.RegisterUserDto;
import com.example.KorkiMedic.entity.PointAction;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.entity.UserPointAction;
import com.example.KorkiMedic.enums.Role;
import com.example.KorkiMedic.repository.PointActionRepository;
import com.example.KorkiMedic.repository.UserPointActionRepository;
import com.example.KorkiMedic.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.KorkiMedic.exceptions.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final PointActionRepository pointActionRepository;
    private final UserPointActionRepository userPointActionRepository;


    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            PointActionRepository pointActionRepository, UserPointActionRepository userPointActionRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.pointActionRepository = pointActionRepository;
        this.userPointActionRepository = userPointActionRepository;
    }

    public User signup(RegisterUserDto input) {
        validatePassword(input.getPassword());
        User user = new User();
        user.setFirstName(input.getFirstName());
        user.setPhoneNumber(input.getPhoneNumber());
        user.setLastName(input.getLastName());
        user.setDateOfBirth(input.getDateOfBirth());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setEmail(input.getEmail());
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public void addDailyLoginPoints(User user) {
        LocalDateTime now = LocalDateTime.now();

        // Pobierz akcję punktową za codzienne logowanie
        PointAction dailyLoginAction = pointActionRepository.findByReason("Codzienne logowanie");

        // Sprawdzenie, czy użytkownik już otrzymał punkty za dzisiejsze logowanie
        boolean alreadyLoggedInToday = userPointActionRepository.existsByUserAndPointActionAndActionDateBetween(
                user,
                dailyLoginAction,
                now.toLocalDate().atStartOfDay(),
                now.toLocalDate().atTime(23, 59, 59)
        );

        if (!alreadyLoggedInToday) {

            // Zapisanie akcji punktowej
            UserPointAction userPointAction = new UserPointAction(null, user, dailyLoginAction, now,10);
            userPointActionRepository.save(userPointAction);

            System.out.println("Dodano 10 punktów za codzienne logowanie użytkownikowi: " + user.getEmail());
        }
    }
    public void validatePassword(String password) {
        // Przykładowa walidacja hasła: minimum 8 znaków, jedna cyfra, jedna litera wielka
        if (password.length() < 8) {
            throw EntityNotFoundException.IllegalPasswordException();
        }
    }
}