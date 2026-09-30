package com._6.creatrove.ledger.dto;

import java.time.LocalDate;

public record LedgerEntryItem(LocalDate date, String item, Long amount, String status) {}