package com.example.KorkiMedic.service;

import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.enums.Role;
import com.example.KorkiMedic.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public boolean isUserDoctor(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        return userOptional.isPresent() && userOptional.get().getRoles().contains(Role.DOCTOR);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public void updateFcmToken(Long userId, String fcmToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setFcmToken(fcmToken);
        userRepository.save(user);
    }
}
