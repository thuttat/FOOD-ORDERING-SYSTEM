package duckie.example.backend.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.RestaurantRequest;
import duckie.example.backend.dto.RestaurantResponse;
import duckie.example.backend.dto.MenuItemResponse;
import duckie.example.backend.entity.Category;
import duckie.example.backend.entity.MenuItem;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.RestaurantStatus;
import duckie.example.backend.entity.User;

@Component
public class RestaurantMapper {

    private final MenuItemMapper menuItemMapper;

    // Tiêm MenuItemMapper vào thông qua Constructor
    public RestaurantMapper(MenuItemMapper menuItemMapper) {
        this.menuItemMapper = menuItemMapper;
    }

    public Restaurant toEntity(RestaurantRequest request, User owner) {
        if (request == null) return null;

        return Restaurant.builder()
                .name(request.name())
                .address(request.address())
                .phone(request.phoneNumber())
                .description(request.description())
                .imageUrl(request.imageUrl())
                .isOpen(request.isOpen() != null ? request.isOpen() : true)
                .owner(owner)
                .status(RestaurantStatus.PENDING)
                .build();
    }

    public RestaurantResponse toResponse(Restaurant restaurant, Double rating) {
        if (restaurant == null) return null;

        List<MenuItemResponse> menuItemsList = new ArrayList<>();
        if (restaurant.getCategories() != null) {
            for (Category category : restaurant.getCategories()) {
                if (category.getMenuItems() != null) {
                    for (MenuItem item : category.getMenuItems()) {
                        menuItemsList.add(menuItemMapper.toResponse(item));
                    }
                }
            }
        }

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getOwner().getId(),
                restaurant.getOwner().getFullname(),
                restaurant.getName(),
                restaurant.getphone(),
                restaurant.getAddress(),
                restaurant.getDescription(),
                restaurant.getImageUrl(),
                restaurant.getIsOpen(),
                restaurant.getStatus(),
                rating != null ? rating : 0.0,
                restaurant.getCreatedAt(),
                restaurant.getUpdatedAt(),
                menuItemsList
        );
    }
}