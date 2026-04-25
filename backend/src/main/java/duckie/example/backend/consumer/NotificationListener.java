package duckie.example.backend.consumer;

import duckie.example.backend.config.RabbitMQConfig;
import duckie.example.backend.entity.Notification;
import duckie.example.backend.entity.User;
import duckie.example.backend.repository.NotificationRepository;
import duckie.example.backend.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Component
public class NotificationListener {
    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public NotificationListener(JavaMailSender mailSender, NotificationRepository notificationRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.mailSender = mailSender;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ADMIN_NOTIFICATION)
    public void handleAdminNotification(String messageJson) {
        try {
            Map<String, String> data = objectMapper.readValue(messageJson, new TypeReference<Map<String, String>>() {});

            User admin = userRepository.findById(2L).orElse(null);
            if (admin != null) {
                Notification notif = Notification.builder()
                        .user(admin)
                        .message(data.get("message"))
                        .type(data.get("type"))
                        .isRead(false)
                        .build();
                notificationRepository.save(notif);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_EMAIL)
    public void handleEmailNotification(String messageJson) {
        try {
            Map<String, String> data = objectMapper.readValue(messageJson, new TypeReference<Map<String, String>>() {});
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(data.get("to"));
            mail.setSubject(data.get("subject"));
            mail.setText(data.get("body"));

            mailSender.send(mail);
            System.out.println(">> The email was successfully sent to: " + data.get("to"));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
