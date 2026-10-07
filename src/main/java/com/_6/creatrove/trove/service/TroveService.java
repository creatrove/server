package com._6.creatrove.trove.service;

import com._6.creatrove.calendar.domain.CalendarEvent;
import com._6.creatrove.calendar.repository.CalendarEventRepository;
import com._6.creatrove.ledger.domain.LedgerEntry;
import com._6.creatrove.ledger.repository.LedgerEntryRepository;
import com._6.creatrove.memo.domain.Memo;
import com._6.creatrove.memo.repository.MemoRepository;
import com._6.creatrove.trove.dto.TroveItemDto;
import com._6.creatrove.trove.dto.TroveItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TroveService {

    private static final DateTimeFormatter MONTH_DAY = DateTimeFormatter.ofPattern("M/d");
    private static final int RECENT_LIMIT = 10;

    private final MemoRepository memoRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    /**
     * 최근 확인한 항목 (메모/캘린더/장부 통합) — viewedAt 기준 내림차순 상위 N개
     */
    public List<TroveItemDto> getRecent(Long userId) {
        List<TroveItemDto> memoItems = memoRepository
                .findTop10ByUser_UserIdAndViewedAtIsNotNullOrderByViewedAtDesc(userId)
                .stream().map(this::toDto).toList();

        List<TroveItemDto> calendarItems = calendarEventRepository
                .findTop10ByUser_UserIdAndViewedAtIsNotNullOrderByViewedAtDesc(userId)
                .stream().map(this::toDto).toList();

        List<TroveItemDto> ledgerItems = ledgerEntryRepository
                .findTop10ByUser_UserIdAndViewedAtIsNotNullOrderByViewedAtDesc(userId)
                .stream().map(this::toDto).toList();

        return Stream.of(memoItems, calendarItems, ledgerItems)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(TroveItemDto::viewedAt).reversed())
                .limit(RECENT_LIMIT)
                .toList();
    }

    /**
     * 통합 검색 — 메모 내용 / 캘린더 제목 / 장부 항목명에 keyword가 포함된 전체 항목
     */
    public List<TroveItemDto> search(Long userId, String keyword) {
        List<TroveItemDto> memoItems = memoRepository
                .findByUser_UserIdAndContentContainingIgnoreCase(userId, keyword)
                .stream().map(this::toDto).toList();

        List<TroveItemDto> calendarItems = calendarEventRepository
                .findByUser_UserIdAndTitleContainingIgnoreCase(userId, keyword)
                .stream().map(this::toDto).toList();

        List<TroveItemDto> ledgerItems = ledgerEntryRepository
                .findByUser_UserIdAndItemContainingIgnoreCase(userId, keyword)
                .stream().map(this::toDto).toList();

        return Stream.of(memoItems, calendarItems, ledgerItems)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(
                        (TroveItemDto dto) -> dto.date() != null ? dto.date() : java.time.LocalDate.MIN
                ).reversed())
                .toList();
    }

    private TroveItemDto toDto(Memo memo) {
        return new TroveItemDto(
                TroveItemType.MEMO,
                memo.getId(),
                memo.getContent(),
                memo.getCreatedAt().toLocalDate(),
                null,
                null,
                null,
                "메모 " + memo.getCategory().displayName(),
                memo.getViewedAt()
        );
    }

    private TroveItemDto toDto(CalendarEvent event) {
        return new TroveItemDto(
                TroveItemType.CALENDAR,
                event.getId(),
                event.getTitle(),
                event.getStartDate(),
                null,
                null,
                null,
                "캘린더 " + event.getStartDate().format(MONTH_DAY),
                event.getViewedAt()
        );
    }

    private TroveItemDto toDto(LedgerEntry entry) {
        return new TroveItemDto(
                TroveItemType.LEDGER,
                entry.getId(),
                entry.getItem(),
                entry.getEntryDate(),
                entry.getItem(),
                entry.getAmount(),
                entry.getStatus().name(),
                "장부 +" + formatAmount(entry.getAmount()) + " " + entry.getStatus().displayName(),
                entry.getViewedAt()
        );
    }

    private String formatAmount(Long amount) {
        return String.format("%,d", amount);
    }
}