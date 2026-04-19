package duckie.example.backend.mapper;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.RestaurantRequest;
import duckie.example.backend.dto.RestaurantResponse;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.RestaurantStatus;
import duckie.example.backend.entity.User;

@Component
public class RestaurantMapper {
    public Restaurant toEntity(RestaurantRequest request, User owner) {
        if (request == null) return null;

        return Restaurant.builder()
                .name(request.name())
                .address(request.address())
                .phoneNumber(request.phoneNumber())
                .description(request.description())
                .imageUrl(request.imageUrl())      
                .isOpen(request.isOpen() != null ? request.isOpen() : true)
                .owner(owner)
                .status(RestaurantStatus.PENDING) 
                .build();
    }

    public RestaurantResponse toResponse(Restaurant restaurant, Double rating) {
        if (restaurant == null) return null;

        return new RestaurantResponse(
            restaurant.getId(),
            restaurant.getOwner().getId(),
            restaurant.getOwner().getFullname(), 
            restaurant.getName(),
            restaurant.getPhoneNumber(),
            restaurant.getAddress(),
            restaurant.getPhoneNumber(), 
            restaurant.getDescription(),
            restaurant.getImageUrl(),
            restaurant.getIsOpen(),
            restaurant.getStatus(),             
            rating != null ? rating : 0.0, 
            restaurant.getCreatedAt(),
            restaurant.getUpdatedAt()
        );
    }
}