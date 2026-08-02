package aston.module4.notification.kafka;

import aston.module4.notification.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final EmailService emailService;

    public NotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void listen(String message) {
        String[] parts = message.split(":", 2);
        if (parts.length < 2) return;

        String operation = parts[0];
        String email = parts[1];

        if ("CREATE".equals(operation)) {
            emailService.sendEmail(email, "Здравствуйте! Ваш аккаунт был успешно создан.");
        } else if ("DELETE".equals(operation)) {
            emailService.sendEmail(email, "Здравствуйте! Ваш аккаунт был удалён.");
        } else if ("UPDATE".equals(operation)) {
            emailService.sendEmail(email, "Здравствуйте! Ваш аккаунт был изменен.");
        }
    }
}