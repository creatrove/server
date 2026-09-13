package com._6.creatrove.calendar.exception;

public class CalendarEventNotFoundException extends RuntimeException {
    public CalendarEventNotFoundException(Long eventId) {
        super("존재하지 않는 일정입니다. id=" + eventId);
    }
}