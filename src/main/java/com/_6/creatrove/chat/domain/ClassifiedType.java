package com._6.creatrove.chat.domain;

public enum ClassifiedType {
    PENDING,     // AI 분류 대기/실패 중
    MEMO,
    CALENDAR,
    LEDGER,
    MULTI,       // 하나의 메시지가 여러 트로브로 분류됨
    AMBIGUOUS,    // AI가 확신 못해 사용자에게 되물어야 함
    DUPLICATE    // 장부 중복 후보 발견, 사용자 선택 대기
}