package com._6.creatrove.memo.domain;

import com._6.creatrove.chat.domain.ChatMessage;
import com._6.creatrove.global.domain.BaseEntity;
import com._6.creatrove.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "memos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_message_id")
    private ChatMessage sourceMessage;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemoCategory category;

    @Column(nullable = false)
    private Boolean pinned;

    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;

    public void markViewed() {
        this.viewedAt = LocalDateTime.now();
    }

    @Builder
    public Memo(User user, ChatMessage sourceMessage, String content, MemoCategory category) {
        this.user = user;
        this.sourceMessage = sourceMessage;
        this.content = content;
        this.category = category;
        this.pinned = false;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void togglePin() {
        this.pinned = !this.pinned;
    }
}