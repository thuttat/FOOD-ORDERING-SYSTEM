package duckie.example.backend.dto;

import java.math.BigDecimal;

public record ChartData(
        String label,
        BigDecimal value
) {
}