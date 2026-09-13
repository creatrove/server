package com._6.creatrove.calendar.domain;

public enum EventCategory {
    WORK, PERSONAL;

    public String displayName() {
        return switch (this) {
            case WORK -> "업무";
            case PERSONAL -> "개인";
        };
    }
}