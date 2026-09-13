package com._6.creatrove.calendar.dto;

import java.time.LocalDate;
import java.util.List;

public record CalendarDayResponse(LocalDate date, List<CalendarEventItem> events) {}