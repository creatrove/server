package com._6.creatrove.user.dto;

import com._6.creatrove.user.domain.UserStatus;

public record UserResponse(
        Long userId,
        String name,
        String email,
        UserStatus status
) {
}