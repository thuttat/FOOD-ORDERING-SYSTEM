package duckie.example.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReviewRequest(
        @NotNull Long orderId,
        @NotNull Long restaurantId,
        @Min(1) @Max(5) int rating,
        String comment
//        List<String> imageUrls
) {}