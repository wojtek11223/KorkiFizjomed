package com.example.KorkiMedic.service;

import com.example.KorkiMedic.dto.ChangePasswordDTO;
import com.example.KorkiMedic.dto.UpdatedUserDTO;
import com.example.KorkiMedic.entity.PointAction;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.entity.UserPointAction;
import com.example.KorkiMedic.enums.Role;
import com.example.KorkiMedic.exceptions.EntityNotFoundException;
import com.example.KorkiMedic.repository.PointActionRepository;
import com.example.KorkiMedic.repository.UserPointActionRepository;
import com.example.KorkiMedic.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationService authenticationService;
    private final UserPointActionRepository userPointActionRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService, UserPointActionRepository userPointActionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.userPointActionRepository = userPointActionRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public boolean isUserDoctor(Long userId) {
        Optional<User> userOptional = userRepository.findByIdAndRoles(userId,Role.DOCTOR);

        return userOptional.isPresent();
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

    public User updateUser(User user, UpdatedUserDTO updatedUserDTO) {
        Optional<User> userWithPhone = userRepository.findByPhoneNumber(updatedUserDTO.getPhoneNumber());
        Optional<User> userWithEmail = userRepository.findByEmail(updatedUserDTO.getEmail());
        if (userWithPhone.isPresent() && !userWithPhone.get().getId().equals(user.getId())) {
            throw EntityNotFoundException.PhoneNumberIsUsedException();
        }
        user.setPhoneNumber(updatedUserDTO.getPhoneNumber());
        if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(user.getId())) {
            throw EntityNotFoundException.EmailIsUsedException();
        }
        user.setEmail(updatedUserDTO.getEmail());
        user.setFirstName(updatedUserDTO.getFirstName());
        user.setLastName(updatedUserDTO.getLastName());
        user.setEmail(updatedUserDTO.getEmail());

        user.setDateOfBirth(updatedUserDTO.getDateOfBirth());
        return userRepository.save(user);
    }

    public void changePassword(User user, ChangePasswordDTO changePasswordDTO) {

        // Sprawdzenie poprawności starego hasła
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw EntityNotFoundException.NotCorrectPasswordException();
        }
        authenticationService.validatePassword(changePasswordDTO.getNewPassword());
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    public  void addLoyalityPoints(User user, int points, PointAction pointAction) {

        UserPointAction userPointAction= new UserPointAction(null,user,pointAction, LocalDateTime.now(),points);
        userPointActionRepository.save(userPointAction);
    }

}
