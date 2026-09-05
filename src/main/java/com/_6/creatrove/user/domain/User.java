package com._6.creatrove.user.domain;

import com._6.creatrove.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = false)
    private Boolean widgetInstalled;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Builder
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.widgetInstalled = false;
        this.status = UserStatus.ACTIVE;
    }

    public void updateProfile(String name) {
        this.name = name;
    }

    public void withdraw() {
        this.status = UserStatus.WITHDRAWN;
    }
}