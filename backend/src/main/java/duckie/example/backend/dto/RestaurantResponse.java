package duckie.example.backend.dto;

import duckie.example.backend.entity.RestaurantStatus;

import java.time.Instant;

public record RestaurantResponse (
        long id,
        String name,
        String ownerName,
        String address,
        String description,
        String imageUrl,
        RestaurantStatus status,
        Instant createdAt
){
}
