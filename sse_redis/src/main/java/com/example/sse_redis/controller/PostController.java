package com.example.sse_redis.controller;

import com.example.sse_redis.dto.PostRequestDto;
import com.example.sse_redis.dto.PostResponseDto;
import com.example.sse_redis.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @PostMapping("/{userId}")
    public PostResponseDto createPost(@PathVariable Long userId, @RequestBody PostRequestDto requestDto) {
        return postService.createPost(userId, requestDto);
    }
}
