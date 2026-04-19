package duckie.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duckie.example.backend.dto.NotificationResponse;
import duckie.example.backend.entity.Notification;
import duckie.example.backend.mapper.NotificationMapper;
import duckie.example.backend.repository.NotificationRepository;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }
}