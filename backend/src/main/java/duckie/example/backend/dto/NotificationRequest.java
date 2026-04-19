package duckie.example.backend.dto;

public record NotificationRequest(
    Long userId,
    String message,
    String type
) {}