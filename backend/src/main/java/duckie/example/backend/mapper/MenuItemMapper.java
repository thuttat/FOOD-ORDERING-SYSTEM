package duckie.example.backend.mapper;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.MenuItemRequest;
import duckie.example.backend.dto.MenuItemResponse;
import duckie.example.backend.entity.Category;
import duckie.example.backend.entity.MenuItem;

@Component
public class MenuItemMapper {
    public MenuItem toEntity(MenuItemRequest request, Category category) {
        if (request == null) return null;
        return MenuItem.builder()
                .category(category)
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .imageUrl(request.imageUrl())
                .isAvailable(request.isAvailable() != null ? request.isAvailable() : true)
                .build();
    }

    public MenuItemResponse toResponse(MenuItem item) {
        if (item == null) return null;
        return new MenuItemResponse(
                item.getId(),
                item.getCategory() != null ? item.getCategory().getName() : "Uncategorized",
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getImageUrl(),
                item.getIsAvailable()
        );
    }
}