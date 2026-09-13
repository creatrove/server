package com._6.creatrove.chat.classifier;

import com._6.creatrove.calendar.domain.EventCategory;

import java.time.LocalDate;
import java.time.LocalTime;

public record CalendarDraft(String title, LocalDate startDate, LocalDate endDate,
                            LocalTime time, EventCategory category) {}