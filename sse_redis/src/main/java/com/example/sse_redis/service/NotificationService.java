package com.example.sse_redis.service;

import com.example.sse_redis.dto.NotificationMessageDto;
import com.example.sse_redis.entity.Notification;
import com.example.sse_redis.entity.User;
import com.example.sse_redis.repository.NotificationRepository;
import com.example.sse_redis.repository.UserRepository;
import com.example.sse_redis.service.message.RedisMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final RedisMessagePublisher messagePublisher;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 구독 관리
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(60 * 1000L);
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        // 초기에 읽지 않은 알림 전송
        sendUnreadNotifications(userId, emitter);

        return emitter;
    }


    private void sendUnreadNotifications(Long userId, SseEmitter emitter){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Notification> unreadNotifications = notificationRepository.getUnreadNotifications(user);

        if(!unreadNotifications.isEmpty()) {
            try {
                emitter.send(SseEmitter.event().
                        name("읽지 않은 메세지입니다.")
                        .data(unreadNotifications));
            } catch (IOException e) {
                // 전송 실패시 유저의 emitter 삭제
                emitters.remove(userId);
            }
        }
    }

    // 알림 발송
    public void notify(Long userId, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification(user, message);
        notificationRepository.save(notification);

        // Redis로 실시간 알림 발송
        messagePublisher.publish(new NotificationMessageDto(userId, message));
    }

    /*
        SseEmitter event의 개념

        서버 측
        event: friend_request
        data: {"from": "user123", "message": "친구 요청이 왔습니다"}

        클라이언트 측
        eventSource.addEventListener('friend_request', event => {
            console.log('친구 요청:', event.data);
        });

        채널: notifications
        이벤트:{
            "type": "friend-request",
            "from": "user123",
            "to": "user456"
        }

        채널은 이벤트들을 그룹화하고 분류하는 논리적 공간, 이벤트는 그 채널을 통해 전달되는 실제 메시지나 데이터
     */
    public void sendToClient(Long userId, String message) {
        SseEmitter emitter = emitters.get(userId);

        // 연결이 끊어진 유저는 return null
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(message));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }
    }

    public void markAsRead(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        notification.markAsRead();
        notificationRepository.save(notification);
    }
}
