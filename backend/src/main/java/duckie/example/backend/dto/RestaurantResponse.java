package duckie.example.backend.dto;

import java.time.Instant;

public record RestaurantResponse(
    Long id,
    Long ownerId,
    String name,
    String address,
    String description,
    Boolean isOpen,
    Instant createdAt,
    Instant updatedAt
) {
}