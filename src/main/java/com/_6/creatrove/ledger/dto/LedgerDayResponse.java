package com._6.creatrove.ledger.dto;

import java.time.LocalDate;
import java.util.List;

public record LedgerDayResponse(LocalDate date, List<LedgerDayEntryItem> entries, LedgerDaySummary summary) {}