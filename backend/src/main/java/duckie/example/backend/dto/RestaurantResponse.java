package duckie.example.backend.dto;

import java.time.Instant;

import duckie.example.backend.entity.RestaurantStatus;

public record RestaurantResponse(
    Long id,
    Long ownerId,
    String ownerName,
    String name,
    String address,
    String description,
    String imageUrl,
    Boolean isOpen,
    RestaurantStatus status,
    Double rating, 
    Instant createdAt,
    Instant updatedAt
) {}

