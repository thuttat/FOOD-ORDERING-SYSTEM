package duckie.example.backend.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MenuItemRequest(
    @NotBlank(message = "Category name cannot be blank")
    String categoryName, 

    @NotBlank(message = "Item name cannot be blank")
    String name,

    String description,

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be greater than zero")
    BigDecimal price,

    String imageUrl,
    
    Boolean isAvailable
) {}