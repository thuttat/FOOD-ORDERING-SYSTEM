package duckie.example.backend.dto;

public record RestaurantRequest (
        String name,
        String phoneNumber,
        String address,
        String description,
        String imageUrl,
        long ownerId
) {
}
