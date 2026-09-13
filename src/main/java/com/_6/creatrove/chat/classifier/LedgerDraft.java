package com._6.creatrove.chat.classifier;

import java.time.LocalDate;

public record LedgerDraft(String item, Long amount, LocalDate date) {}