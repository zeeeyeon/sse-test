package com.example.sse_redis.service.message;

import com.example.sse_redis.dto.NotificationMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessagePublisher {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String CHANNEL = "notifications";

    public void publish(NotificationMessageDto message) {
        redisTemplate.convertAndSend(CHANNEL, message.serialize());
    }
}
