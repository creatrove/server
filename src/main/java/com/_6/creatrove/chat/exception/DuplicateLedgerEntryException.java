package com._6.creatrove.chat.exception;

public class DuplicateLedgerEntryException extends RuntimeException {
    private final Long existingEntryId;

    public DuplicateLedgerEntryException(Long existingEntryId, Long amount) {
        super("이미 같은 금액(%,d원)의 예정 수입이 있습니다. id=%d".formatted(amount, existingEntryId));
        this.existingEntryId = existingEntryId;
    }

    public Long getExistingEntryId() {
        return existingEntryId;
    }
}