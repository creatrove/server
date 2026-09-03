package com._6.creatrove.user.controller;

import com._6.creatrove.user.domain.User;
import com._6.creatrove.user.dto.UserResponse;
import com._6.creatrove.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/api/users/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return ResponseEntity.ok(new UserResponse(
                user.getUserId(), user.getName(), user.getEmail(), user.getStatus()
        ));
    }
}