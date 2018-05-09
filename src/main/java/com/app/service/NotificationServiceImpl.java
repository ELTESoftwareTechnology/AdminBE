package com.app.service;

import com.app.entity.Notification;
import com.app.entity.User;
import com.app.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{

    private NotificationRepository notificationRepository;

    @Autowired
    public void setNotificationRepository(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> findNotificationsForEmail(String email) {
        return notificationRepository.findByEmail(email);
    }
}
