package com._6.creatrove.trove.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TroveItemType {
    MEMO("메모"),
    CALENDAR("캘린더"),
    LEDGER("장부");

    private final String displayName;
}