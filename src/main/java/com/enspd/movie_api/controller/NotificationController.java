package com.enspd.movie_api.controller;

import com.enspd.movie_api.model.Notification;
import com.enspd.movie_api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications(Authentication authentication) {
        String userId = getUserIdFromAuthentication(authentication);
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(Authentication authentication) {
        String userId = getUserIdFromAuthentication(authentication);
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable String id) {
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/read-all")
    public ResponseEntity<Void> markAllNotificationsAsRead(Authentication authentication) {
        String userId = getUserIdFromAuthentication(authentication);
        notificationService.markAllNotificationsAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadNotificationCount(Authentication authentication) {
        String userId = getUserIdFromAuthentication(authentication);
        return ResponseEntity.ok(notificationService.getUnreadNotificationCount(userId));
    }

    private String getUserIdFromAuthentication(Authentication authentication) {
        return ((com.enspd.movie_api.model.User) authentication.getPrincipal()).getId();
    }
} 