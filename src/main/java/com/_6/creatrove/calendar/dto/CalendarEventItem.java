package com._6.creatrove.calendar.dto;

import java.time.LocalTime;

public record CalendarEventItem(Long eventId, String title, String category,
                                LocalTime eventTime, LocalTime eventEndTime) {}