package com.example.sse_redis.repository;

import com.example.sse_redis.entity.Notification;
import com.example.sse_redis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.isRead = false ORDER BY n.createdAt DESC")
    List<Notification> getUnreadNotifications(@Param("user") User user);

    @Query("SELECT n FROM Notification n WHERE n.user = :user ORDER BY n.createdAt DESC")
    List<Notification> getNotifications(@Param("user") User user);
}
