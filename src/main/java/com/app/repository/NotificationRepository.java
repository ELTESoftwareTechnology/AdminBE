package com.app.repository;

import com.app.entity.Notification;
import com.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    /**
     * Find notifications that targets an email
     * @param email to look for
     * @return notifications for a targeted email
     */
    List<Notification> findByEmail(String email);
}