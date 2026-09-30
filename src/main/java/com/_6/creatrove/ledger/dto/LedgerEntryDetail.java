package com._6.creatrove.ledger.dto;

import java.time.LocalDate;

public record LedgerEntryDetail(Long entryId, String item, Long amount, String status, LocalDate entryDate) {}