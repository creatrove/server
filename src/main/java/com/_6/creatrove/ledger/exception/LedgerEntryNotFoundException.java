package com._6.creatrove.ledger.exception;

public class LedgerEntryNotFoundException extends RuntimeException {
    public LedgerEntryNotFoundException(Long entryId) {
        super("존재하지 않는 수입 항목입니다. id=" + entryId);
    }
}