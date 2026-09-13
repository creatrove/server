package com._6.creatrove.calendar.domain;

import com._6.creatrove.chat.domain.ChatMessage;
import com._6.creatrove.global.domain.BaseEntity;
import com._6.creatrove.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Table(name = "calendar_events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_message_id")
    private ChatMessage sourceMessage;

    @Column(nullable = false)
    private String title;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "event_time")
    private LocalTime eventTime; // nullable: "시간 없음" 토글 대응

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventSource source;

    @Column(name = "external_event_id")
    private String externalEventId; // 구글/애플 연동 시 중복 방지용 원본 ID

    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;

    public void markViewed() {
        this.viewedAt = LocalDateTime.now();
    }

    @Column(name = "event_end_time")
    private LocalTime eventEndTime; // nullable: 종료 시간 없는 일정(마감일 등)도 있음

    @Builder
    public CalendarEvent(User user, ChatMessage sourceMessage, String title,
                         LocalDate eventDate, LocalTime eventTime, LocalTime eventEndTime, EventSource source) {
        this.user = user;
        this.sourceMessage = sourceMessage;
        this.title = title;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventEndTime = eventEndTime;
        this.source = source;
    }

    public void update(String title, LocalDate eventDate, LocalTime eventTime, LocalTime eventEndTime) {
        this.title = title;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventEndTime = eventEndTime;
    }
}