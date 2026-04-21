package duckie.example.backend.mapper;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.CategoryRequest;
import duckie.example.backend.dto.CategoryResponse;
import duckie.example.backend.entity.Category;
import duckie.example.backend.entity.Restaurant;

@Component
public class CategoryMapper {
    public Category toEntity(CategoryRequest request, Restaurant restaurant) {
        if (request == null) return null;
        return Category.builder()
                .restaurant(restaurant)
                .name(request.name())
                .build();
    }

    public CategoryResponse toResponse(Category category) {
        if (category == null) return null;
        return new CategoryResponse(
                category.getId(),
                category.getRestaurant().getId(),
                category.getName(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}