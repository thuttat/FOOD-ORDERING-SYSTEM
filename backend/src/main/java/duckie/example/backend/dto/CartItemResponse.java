package duckie.example.backend.dto;

import java.math.BigDecimal;

public record CartItemResponse(
    Long id,
    Long menuItemId,
    String itemName,
    String imageUrl,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal totalPrice 
) {}