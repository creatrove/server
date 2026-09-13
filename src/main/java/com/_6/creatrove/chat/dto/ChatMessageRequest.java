package com._6.creatrove.chat.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageRequest(
        @NotBlank(message = "메시지 내용을 입력해주세요.") String content,
        String imageUrl,
        String linkUrl
) {}