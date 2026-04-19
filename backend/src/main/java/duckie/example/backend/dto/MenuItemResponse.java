package duckie.example.backend.dto;

import java.math.BigDecimal;

public record MenuItemResponse(
    Long id,
    Long categoryId,
    String name,
    String description,
    BigDecimal price,
    String imageUrl,
    Boolean isAvailable
) {}