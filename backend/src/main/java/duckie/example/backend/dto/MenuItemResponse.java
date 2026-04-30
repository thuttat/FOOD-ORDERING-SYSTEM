package duckie.example.backend.dto;

import java.math.BigDecimal;

public record MenuItemResponse(
    Long id,
    String categoryName,
    String name,
    String description,
    BigDecimal price,
    String imageUrl,
    Boolean isAvailable
) {}