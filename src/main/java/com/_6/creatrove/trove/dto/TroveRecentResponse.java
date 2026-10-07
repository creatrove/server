package com._6.creatrove.trove.dto;

import java.util.List;

public record TroveRecentResponse(
        List<TroveItemDto> items
) {
}