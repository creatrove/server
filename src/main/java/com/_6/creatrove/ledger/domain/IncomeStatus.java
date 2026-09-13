package com._6.creatrove.ledger.domain;

public enum IncomeStatus {
    COMPLETED, SCHEDULED;

    public String displayName() {
        return switch (this) {
            case COMPLETED -> "완료";
            case SCHEDULED -> "예정";
        };
    }
}