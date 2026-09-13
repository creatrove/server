package com._6.creatrove.calendar.repository;

import com._6.creatrove.calendar.domain.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
}