package com._6.creatrove.ledger.dto;

import java.util.List;

public record LedgerMonthResponse(int year, int month, long totalIncome, List<LedgerEntryItem> entries) {}