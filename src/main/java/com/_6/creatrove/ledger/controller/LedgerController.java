package com._6.creatrove.ledger.controller;

import com._6.creatrove.ledger.dto.LedgerDayResponse;
import com._6.creatrove.ledger.dto.LedgerEntryDetail;
import com._6.creatrove.ledger.dto.LedgerMonthResponse;
import com._6.creatrove.ledger.service.LedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService ledgerService;

    @GetMapping("/ledger")
    public LedgerMonthResponse getMonth(@AuthenticationPrincipal Long userId,
                                        @RequestParam int year,
                                        @RequestParam int month) {
        return ledgerService.getMonth(userId, year, month);
    }

    @GetMapping("/ledger/day")
    public LedgerDayResponse getDay(@AuthenticationPrincipal Long userId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ledgerService.getDay(userId, date);
    }

    @GetMapping("/ledger/entries/{entryId}")
    public LedgerEntryDetail getEntry(@AuthenticationPrincipal Long userId, @PathVariable Long entryId) {
        return ledgerService.getEntry(userId, entryId);
    }
}