package com.example.sse_redis.controller;

import com.example.sse_redis.dto.UserRequestDto;
import com.example.sse_redis.entity.User;
import com.example.sse_redis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody UserRequestDto request) {
        User user = new User(request.getUsername(), request.getPassword());
        return userRepository.save(user);
    }
}
