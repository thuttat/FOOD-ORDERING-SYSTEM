package duckie.example.backend.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.CartItemRequest;
import duckie.example.backend.dto.CartItemResponse;
import duckie.example.backend.entity.Cart;
import duckie.example.backend.entity.CartItem;
import duckie.example.backend.entity.MenuItem;

@Component
public class CartItemMapper {
    public CartItem toEntity(CartItemRequest request, Cart cart, MenuItem menuItem) {
        if (request == null) return null;
        return CartItem.builder()
                .cart(cart)
                .menuItem(menuItem)
                .quantity(request.quantity())
                .build();
    }

    public CartItemResponse toResponse(CartItem item) {
        if (item == null) return null;
        BigDecimal unitPrice = item.getMenuItem().getPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

        return new CartItemResponse(
                item.getId(),
                item.getMenuItem().getId(),
                item.getMenuItem().getName(),
                item.getMenuItem().getImageUrl(),
                item.getQuantity(),
                unitPrice,
                totalPrice 
        );
    }
}