package com._6.creatrove.calendar.controller;

import com._6.creatrove.calendar.dto.CalendarDayResponse;
import com._6.creatrove.calendar.dto.CalendarEventDetail;
import com._6.creatrove.calendar.dto.CalendarMonthResponse;
import com._6.creatrove.calendar.service.CalendarService;
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
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/calendar/events")
    public CalendarMonthResponse getMonth(@AuthenticationPrincipal Long userId,
                                          @RequestParam int year,
                                          @RequestParam int month) {
        return calendarService.getMonth(userId, year, month);
    }

    @GetMapping("/calendar/events/day")
    public CalendarDayResponse getDay(@AuthenticationPrincipal Long userId,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return calendarService.getDay(userId, date);
    }

    @GetMapping("/calendar/events/{eventId}")
    public CalendarEventDetail getEvent(@AuthenticationPrincipal Long userId, @PathVariable Long eventId) {
        return calendarService.getEvent(userId, eventId);
    }
}