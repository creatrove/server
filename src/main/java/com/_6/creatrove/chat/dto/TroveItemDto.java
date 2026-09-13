package com._6.creatrove.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TroveItemDto(
        String type,
        Long id,
        String title,
        String date,
        String item,
        Long amount,
        String status,
        String label
) {}