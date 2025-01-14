package com.example.sse_test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class EmitterService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper;

    public void addEmitter(SseEmitter emitter) {
        // 새로운 SSE 연결
        emitters.add(emitter);
        // 완료 콜백, emitters 리스트에서 emitter 제거
        emitter.onCompletion(() -> emitters.remove(emitter));
        // 시간 초과 콜백, emitters 리스트에서 emitter 제거
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    @Scheduled(fixedRate = 1000)
    public void sendEvent() {
        for (SseEmitter emitter : emitters) {
            try {
                // 문자열 데이터 전송
                emitter.send("연결 완료.");

                // JSON 데이터 전송
                Map<String, Object> eventData = new HashMap<>();
                eventData.put("message", "Hello, world!");
                eventData.put("timestamp", System.currentTimeMillis());
                String json = objectMapper.writeValueAsString(eventData);
                emitter.send(json, MediaType.APPLICATION_JSON);

            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }
}
