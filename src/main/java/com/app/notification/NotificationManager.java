package com.app.notification;

import com.app.entity.Notification;
import com.app.entity.User;
import com.app.service.ChunkService;
import com.app.service.NotificationService;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

@Component
public class NotificationManager {

    public enum NotificationType {
        Registration,
        Invitation,
        DataReceived,
        EvaluationReceived
    }

    private NotificationService notificationService;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void sendMail(NotificationType type, User from, String email, Map<String, Object> data){
        switch (type) {
            case Registration:
                sendMail(email,
                        "Thank you for registering!",
                        "Welcome to the MedicalBox System. We hope you enjoy your stay!");
                break;
            case Invitation:
                sendMail(email,
                        "You have been invited to MedicalBox",
                        "One of your patient shared their medical data with you. Please click on the link below to register and view it: http://medicalbox.online");
                break;
            case DataReceived:
                sendMail(email,
                        "You have got new medical data",
                        "One of your patient shared their medical data with you. Log in to the MedicalBox system to view it: http://medicalbox.online");
                break;
        }

        Notification notification = new Notification(0L, from, email, type.name(), null);
        notificationService.saveNotification(notification);
    }

    public void sendMail(String email, String subject, String content){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.zoho.eu");
        props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("daniel@medicalbox.online","MedicalBox777");
                    }});

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("daniel@medicalbox.online"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
