package com._6.creatrove.calendar.dto;

import java.util.List;

public record CalendarMonthResponse(int year, int month, List<CalendarDaySummary> events) {}