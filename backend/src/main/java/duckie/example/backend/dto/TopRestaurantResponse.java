package duckie.example.backend.dto;

import java.math.BigDecimal;

public record TopRestaurantResponse(
        Long id,
        String name,
        long orderCount,
        BigDecimal revenue,
        Double rating
) {
}
