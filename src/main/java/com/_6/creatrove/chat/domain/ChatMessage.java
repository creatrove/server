package com._6.creatrove.chat.domain;

import com._6.creatrove.global.domain.BaseEntity;
import com._6.creatrove.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "link_url")
    private String linkUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "classified_type", nullable = false)
    private ClassifiedType classifiedType;

    @Column(nullable = false)
    private Boolean pinned;

    @Builder
    public ChatMessage(User user, String content, String imageUrl, String linkUrl) {
        this.user = user;
        this.content = content;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.classifiedType = ClassifiedType.PENDING;
        this.pinned = false;
    }

    public void updateClassification(ClassifiedType classifiedType) {
        this.classifiedType = classifiedType;
    }

    public void pin() {
        this.pinned = true;
    }

    public void unpin() {
        this.pinned = false;
    }
}