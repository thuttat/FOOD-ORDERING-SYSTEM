package duckie.example.backend.mapper;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.NotificationRequest;
import duckie.example.backend.dto.NotificationResponse;
import duckie.example.backend.entity.Notification;
import duckie.example.backend.entity.User;

@Component
public class NotificationMapper {
    public Notification toEntity(NotificationRequest request, User user) {
        if (request == null) return null;
        return Notification.builder()
                .user(user)
                .message(request.message())
                .type(request.type())
                .isRead(false)
                .build();
    }
    public NotificationResponse toResponse(Notification notification) {
        if (notification == null) return null;
        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.getIsRead(),
                notification.getType(),
                notification.getCreatedAt()
        );
    }
}