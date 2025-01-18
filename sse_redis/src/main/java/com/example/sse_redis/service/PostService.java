package com.example.sse_redis.service;

import com.example.sse_redis.dto.PostRequestDto;
import com.example.sse_redis.dto.PostResponseDto;
import com.example.sse_redis.entity.Post;
import com.example.sse_redis.entity.User;
import com.example.sse_redis.repository.PostRepository;
import com.example.sse_redis.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public PostResponseDto createPost(Long userId, PostRequestDto requestDto) {
        try {
            User author = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Post post = Post.builder()
                    .author(author)
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .build();

            Post savedPost = postRepository.save(post);

            String message = String.format("%s님이 새 글을 작성했습니다: %s",
                    author.getUsername(),
                    post.getTitle());

            try {
                userRepository.findByIdNot(userId)
                        .forEach(user ->
                                notificationService.notify(user.getId(), message)
                        );
            } catch (Exception e) {
            }

            return new PostResponseDto(savedPost);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create post", e);
        }
    }
}