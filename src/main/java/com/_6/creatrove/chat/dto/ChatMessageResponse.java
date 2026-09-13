package com._6.creatrove.chat.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ChatMessageResponse(
        Long messageId,
        String content,
        String classifiedType,
        LocalDateTime createdAt,
        TroveItemDto troveItem,
        List<TroveItemDto> troveItems,
        List<String> suggestedCategories
) {}