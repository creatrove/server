package com._6.creatrove.memo.domain;

public enum MemoCategory {
    IDEA, SCRIPT, REFERENCE;

    public String displayName() {
        return switch (this) {
            case IDEA -> "아이디어";
            case SCRIPT -> "대본";
            case REFERENCE -> "레퍼런스";
        };
    }
}