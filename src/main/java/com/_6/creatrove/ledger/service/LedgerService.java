package com._6.creatrove.ledger.service;

import com._6.creatrove.ledger.domain.IncomeStatus;
import com._6.creatrove.ledger.domain.LedgerEntry;
import com._6.creatrove.ledger.dto.*;
import com._6.creatrove.ledger.exception.LedgerEntryNotFoundException;
import com._6.creatrove.ledger.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LedgerService {

    private final LedgerEntryRepository ledgerEntryRepository;

    public LedgerMonthResponse getMonth(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        var entries = ledgerEntryRepository
                .findByUser_UserIdAndEntryDateBetweenOrderByEntryDateAsc(userId, start, end);

        long totalIncome = entries.stream()
                .filter(e -> e.getStatus() == IncomeStatus.COMPLETED)
                .mapToLong(LedgerEntry::getAmount)
                .sum();

        var items = entries.stream()
                .map(e -> new LedgerEntryItem(e.getEntryDate(), e.getItem(), e.getAmount(), e.getStatus().name()))
                .toList();

        return new LedgerMonthResponse(year, month, totalIncome, items);
    }

    public LedgerDayResponse getDay(Long userId, LocalDate date) {
        var entries = ledgerEntryRepository
                .findByUser_UserIdAndEntryDateOrderByCreatedAtAsc(userId, date);

        long completed = entries.stream()
                .filter(e -> e.getStatus() == IncomeStatus.COMPLETED)
                .mapToLong(LedgerEntry::getAmount)
                .sum();

        long scheduled = entries.stream()
                .filter(e -> e.getStatus() == IncomeStatus.SCHEDULED)
                .mapToLong(LedgerEntry::getAmount)
                .sum();

        var items = entries.stream()
                .map(e -> new LedgerDayEntryItem(e.getId(), e.getItem(), e.getAmount(), e.getStatus().name()))
                .toList();

        return new LedgerDayResponse(date, items, new LedgerDaySummary(completed, scheduled));
    }

    @Transactional // 클래스 레벨 readOnly = true를 이 메서드만 override (viewedAt 갱신 위해)
    public LedgerEntryDetail getEntry(Long userId, Long entryId) {
        LedgerEntry entry = ledgerEntryRepository.findById(entryId)
                .filter(e -> e.getUser().getUserId().equals(userId))
                .orElseThrow(() -> new LedgerEntryNotFoundException(entryId));

        entry.markViewed();

        return new LedgerEntryDetail(entry.getId(), entry.getItem(), entry.getAmount(),
                entry.getStatus().name(), entry.getEntryDate());
    }
}