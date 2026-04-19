package duckie.example.backend.dto;

import java.time.Instant;

public record CategoryResponse(
    Long id,
    Long restaurantId,
    String name,
    Instant createdAt,
    Instant updatedAt
) {}