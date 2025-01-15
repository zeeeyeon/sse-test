package com.example.sse_redis.service.message;

import com.example.sse_redis.dto.NotificationMessageDto;
import com.example.sse_redis.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {
    private final NotificationService notificationService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        NotificationMessageDto notification = null;
        try {
            notification = NotificationMessageDto.deserialize(
                    new String(message.getBody())
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        notificationService.sendToClient(
                notification.getUserId(),
                notification.getMessage()
        );

    }
}