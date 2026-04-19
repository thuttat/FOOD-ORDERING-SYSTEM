package duckie.example.backend.dto;

import java.time.Instant;

public record ReviewResponse(
    Long id,
    Long restaurantId,
    Long customerId,
    String customerName,
    Integer rating,
    String comment,
    Instant createdAt
) {}