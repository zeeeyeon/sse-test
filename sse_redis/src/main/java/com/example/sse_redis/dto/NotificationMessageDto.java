package com.example.sse_redis.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationMessageDto {
    private Long userId;
    private String message;

    public String serialize() {
        return String.format("{\"userId\":%d,\"message\":\"%s\"}", userId, message);
    }

    public static NotificationMessageDto deserialize(String json) throws JsonProcessingException {
        JsonNode node = new ObjectMapper().readTree(json);
        return new NotificationMessageDto(
                node.get("userId").asLong(),
                node.get("message").asText()
        );
    }
}
