package com.example.sse_redis.controller;

import com.example.sse_redis.dto.NotificationMessageDto;
import com.example.sse_redis.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    // 실시간 알림을 받기 위한 SSE 연결 설정
    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId) {
        return notificationService.subscribe(userId);
    }

    // 특정 기능 사용자에게 알림 전송
    @PostMapping
    public void sendNotification(@RequestBody NotificationMessageDto message) {
        notificationService.notify(message.getUserId(), message.getMessage());
    }

    @PatchMapping("/{userId}/{notificationId}/read")
    public void markAsRead(@PathVariable Long userId, @PathVariable Long notificationId) {
        notificationService.markAsRead(userId, notificationId);
    }

    /*
    클라이언트 측 SSE 연결
    const eventSource = new EventSource('/notifications/subscribe/123');

    알림 전송
    fetch('notifications', {
        method: 'POST',
        body: JSON.stringify({
            userId: 123,
            message: "새로운 알림입니다"
        })
    });

    알림 읽음 처리
    fetch('/notifications/123/456/read', {
        method: 'PATCH'
    });
     */
}
