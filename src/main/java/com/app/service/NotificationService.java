package com.app.service;

import com.app.entity.Notification;
import com.app.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    Notification saveNotification(Notification notification);
    List<Notification> findNotificationsForEmail(String email);
}
