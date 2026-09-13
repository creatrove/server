package com._6.creatrove.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TroveItemDto(
        String type,     // MEMO, CALENDAR, LEDGER
        Long id,
        String title,    // CALENDAR/MEMO용
        String date,     // CALENDAR용
        String item,     // LEDGER용
        Long amount,      // LEDGER용
        String status     // LEDGER용
) {}