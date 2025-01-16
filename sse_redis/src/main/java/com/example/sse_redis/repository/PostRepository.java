package com.example.sse_redis.repository;

import com.example.sse_redis.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
