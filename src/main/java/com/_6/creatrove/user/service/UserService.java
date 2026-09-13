package com._6.creatrove.user.service;

import com._6.creatrove.user.domain.User;
import com._6.creatrove.user.dto.UpdateProfileRequest;
import com._6.creatrove.user.dto.UserResponse;
import com._6.creatrove.user.exception.UserNotFoundException;
import com._6.creatrove.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.updateProfile(request.name());

        return toResponse(user);
    }

    @Transactional
    public UserResponse completeOnboarding(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.completeOnboarding();

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getUserId(), user.getName(), user.getEmail(), user.getOnboardingCompleted(),
                user.getStatus());
    }
}