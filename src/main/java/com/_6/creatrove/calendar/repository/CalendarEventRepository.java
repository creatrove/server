package com._6.creatrove.calendar.repository;

import com._6.creatrove.calendar.domain.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    // 월 범위와 겹치는 일정: startDate <= 월말 AND endDate >= 월초
    List<CalendarEvent> findByUser_UserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEventTimeAsc(
            Long userId, LocalDate monthEnd, LocalDate monthStart);

    // 특정 날짜를 포함하는 일정: startDate <= date AND endDate >= date
    List<CalendarEvent> findByUser_UserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByEventTimeAsc(
            Long userId, LocalDate date1, LocalDate date2);

    List<CalendarEvent> findTop10ByUser_UserIdAndViewedAtIsNotNullOrderByViewedAtDesc(Long userId);

    List<CalendarEvent> findByUser_UserIdAndTitleContainingIgnoreCase(Long userId, String keyword);
}