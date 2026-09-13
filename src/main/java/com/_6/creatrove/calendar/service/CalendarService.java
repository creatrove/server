package com._6.creatrove.calendar.service;

import com._6.creatrove.calendar.domain.CalendarEvent;
import com._6.creatrove.calendar.dto.*;
import com._6.creatrove.calendar.exception.CalendarEventNotFoundException;
import com._6.creatrove.calendar.repository.CalendarEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

    private final CalendarEventRepository calendarEventRepository;

    public CalendarMonthResponse getMonth(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate monthStart = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();

        List<CalendarEvent> events = calendarEventRepository
                .findByUser_UserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEventTimeAsc(
                        userId, monthEnd, monthStart);

        // 여러 날짜에 걸친 일정을, 그 기간에 포함된 날짜마다 라벨로 펼쳐 담는다
        Map<LocalDate, List<String>> titlesByDate = new TreeMap<>();
        for (CalendarEvent event : events) {
            LocalDate rangeStart = event.getStartDate().isBefore(monthStart) ? monthStart : event.getStartDate();
            LocalDate rangeEnd = event.getEndDate().isAfter(monthEnd) ? monthEnd : event.getEndDate();

            for (LocalDate date = rangeStart; !date.isAfter(rangeEnd); date = date.plusDays(1)) {
                titlesByDate.computeIfAbsent(date, d -> new ArrayList<>()).add(event.getTitle());
            }
        }

        List<CalendarDaySummary> summaries = titlesByDate.entrySet().stream()
                .map(entry -> new CalendarDaySummary(entry.getKey(), entry.getValue().size(), entry.getValue()))
                .toList();

        return new CalendarMonthResponse(year, month, summaries);
    }

    public CalendarDayResponse getDay(Long userId, LocalDate date) {
        List<CalendarEvent> events = calendarEventRepository
                .findByUser_UserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByEventTimeAsc(
                        userId, date, date);

        List<CalendarEventItem> items = events.stream()
                .map(e -> new CalendarEventItem(e.getId(), e.getTitle(), e.getCategory().name(),
                        e.getEventTime(), e.getEventEndTime()))
                .toList();

        return new CalendarDayResponse(date, items);
    }

    @Transactional
    public CalendarEventDetail getEvent(Long userId, Long eventId) {
        CalendarEvent event = calendarEventRepository.findById(eventId)
                .filter(e -> e.getUser().getUserId().equals(userId))
                .orElseThrow(() -> new CalendarEventNotFoundException(eventId));

        event.markViewed();

        return new CalendarEventDetail(event.getId(), event.getTitle(), event.getCategory().name(),
                event.getStartDate(), event.getEndDate(),
                event.getEventTime(), event.getEventEndTime(), event.getSource().name());
    }
}