package duckie.example.backend.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
    Long id,
    Long menuItemId,
    String itemName,
    Integer quantity,
    BigDecimal unitPrice
) {
}