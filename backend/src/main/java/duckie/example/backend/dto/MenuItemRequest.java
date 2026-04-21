package duckie.example.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MenuItemRequest(
    @NotNull(message = "Danh mục không được để trống")
    Long categoryId,

    @NotBlank(message = "Tên món ăn không được để trống")
    String name,

    String description,

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    BigDecimal price,

    String imageUrl,
    Boolean isAvailable
) {}
