package com.example.sse_redis.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String message;
    private LocalDateTime createdAt;
    private boolean isRead;

    @Builder
    public Notification(User user, String message) {
        this.user = user;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
