package duckie.example.backend.service;

import org.springframework.stereotype.Component;
import duckie.example.backend.dto.RestaurantRequest;
import duckie.example.backend.dto.RestaurantResponse;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.User;

@Component
public class RestaurantMapper {

    public Restaurant toEntity(RestaurantRequest request, User owner) {
        if (request == null) {
            return null;
        }

        return Restaurant.builder()
                .name(request.name())
                .address(request.address())
                .description(request.description())
                .isOpen(request.isOpen() != null ? request.isOpen() : true)
                .owner(owner)
                .build();
    }

    public RestaurantResponse toResponse(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        return new RestaurantResponse(
            restaurant.getId(),
            restaurant.getOwner().getId(),
            restaurant.getName(),
            restaurant.getAddress(),
            restaurant.getDescription(),
            restaurant.getIsOpen(),
            restaurant.getCreatedAt(),
            restaurant.getUpdatedAt()
        );
    }
}