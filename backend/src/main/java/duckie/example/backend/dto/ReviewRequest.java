package duckie.example.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewRequest(
        @NotNull Long orderId,
        Long restaurantId,
        Long menuItemId,
        @Min(1) @Max(5) int rating,
        String comment
) {}