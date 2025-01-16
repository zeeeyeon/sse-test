package com.example.sse_redis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
class SseRedisApplicationTests {

    @Test
    void contextLoads() {
    }

}
