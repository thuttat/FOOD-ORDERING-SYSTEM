package duckie.example.backend.dto;

import duckie.example.backend.entity.OrderStatus;

public record StatusChartData(
        OrderStatus status,
        long count
) {
}