package com._6.creatrove.calendar.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CalendarEventDetail(Long eventId, String title, String category,
                                  LocalDate startDate, LocalDate endDate,
                                  LocalTime eventTime, LocalTime eventEndTime, String source) {}