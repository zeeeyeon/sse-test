package com.example.sse_redis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequestDto {
    private String username;
    private String password;
}
