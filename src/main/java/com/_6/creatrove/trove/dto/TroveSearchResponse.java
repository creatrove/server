package com._6.creatrove.trove.dto;

import java.util.List;

public record TroveSearchResponse(
        String keyword,
        List<TroveItemDto> items
) {
}