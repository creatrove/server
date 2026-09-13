package com._6.creatrove.chat.classifier;

import java.time.LocalDate;
import java.time.LocalTime;

public record CalendarDraft(String title, LocalDate date, LocalTime time) {}