package com.example.sse_test.controller;

import com.example.sse_test.service.EmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class EmitterController {

    private final EmitterService emitterService;

    @GetMapping(path = "/emitter", produces = MediaType.TEXT_EVENT_STREAM_VALUE)

    public SseEmitter sub() {
        SseEmitter emitter  = new SseEmitter();
        emitterService.addEmitter(emitter);
        emitterService.sendEvent();
        return emitter;
    }
}
