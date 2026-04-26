package duckie.example.backend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
    Long id,

    Long customerId,

    Long restaurantId,
    String restaurantName,
    BigDecimal totalAmount,

    BigDecimal deliveryFee,

    String status,

    String deliveryAddress,

    String customerNote,

    List<OrderItemResponse> items,

    Instant createdAt
) {
}