package com._6.creatrove.calendar.dto;

import java.time.LocalDate;
import java.util.List;

public record CalendarDaySummary(LocalDate date, int count, List<String> labels) {}