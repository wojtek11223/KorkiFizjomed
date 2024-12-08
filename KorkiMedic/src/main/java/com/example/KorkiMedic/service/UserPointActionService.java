package com.example.KorkiMedic.service;

import com.example.KorkiMedic.dto.UserPointActionDTO;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.entity.UserPointAction;
import com.example.KorkiMedic.repository.UserPointActionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPointActionService {

    private final UserPointActionRepository userPointActionRepository;

    public UserPointActionService(UserPointActionRepository userPointActionRepository) {
        this.userPointActionRepository = userPointActionRepository;
    }

    public List<UserPointActionDTO> getUserPointActions(User user) {
        List<UserPointAction> actions = userPointActionRepository.findByUser(user);
        return actions.stream()
                .map(action -> new UserPointActionDTO(
                        action.getPointAction().getReason(),
                        action.getPoints(),
                        action.getActionDate()
                ))
                .collect(Collectors.toList());
    }
}
