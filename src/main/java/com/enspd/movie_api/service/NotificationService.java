package com.enspd.movie_api.service;

import com.enspd.movie_api.model.Movie;
import com.enspd.movie_api.model.Notification;
import com.enspd.movie_api.model.User;
import com.enspd.movie_api.repository.NotificationRepository;
import com.enspd.movie_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void notifyNewMovie(Movie movie) {
        List<User> users = userRepository.findAll();
        String message = "New movie added: " + movie.getTitle();

        for (User user : users) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setMovie(movie);
            notification.setMessage(message);
            notificationRepository.save(notification);

            // Send real-time notification via WebSocket
            messagingTemplate.convertAndSendToUser(
                user.getUsername(),
                "/topic/notifications",
                message
            );
        }
    }

    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadNotifications(String userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public void markNotificationAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Transactional
    public void markAllNotificationsAsRead(String userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
        unreadNotifications.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    public long getUnreadNotificationCount(String userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }
} 