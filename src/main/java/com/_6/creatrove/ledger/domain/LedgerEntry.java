package com._6.creatrove.ledger.domain;

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

@Entity
@Getter
@Table(name = "ledger_entries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LedgerEntry extends BaseEntity {

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
    private String item;

    @Column(nullable = false)
    private Long amount; // 원 단위, 소수점 없음

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncomeStatus status;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;

    public void markViewed() {
        this.viewedAt = LocalDateTime.now();
    }

    @Builder
    public LedgerEntry(User user, ChatMessage sourceMessage, String item,
                       Long amount, IncomeStatus status, LocalDate entryDate) {
        this.user = user;
        this.sourceMessage = sourceMessage;
        this.item = item;
        this.amount = amount;
        this.status = status;
        this.entryDate = entryDate;
    }

    public void updateStatus(IncomeStatus status) {
        this.status = status;
    }

    public void update(String item, Long amount) {
        this.item = item;
        this.amount = amount;
    }
}