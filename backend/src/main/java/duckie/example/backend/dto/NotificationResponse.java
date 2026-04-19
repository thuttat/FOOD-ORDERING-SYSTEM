package duckie.example.backend.dto;

import java.time.Instant;

public record NotificationResponse(
    Long id,
    String message,
    Boolean isRead,
    String type,
    Instant createdAt
) {}