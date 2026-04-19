package duckie.example.backend.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.CartItemResponse;
import duckie.example.backend.dto.CartResponse;
import duckie.example.backend.entity.Cart;
import duckie.example.backend.entity.CartItem;

@Component
public class CartMapper {
    private final CartItemMapper cartItemMapper;

    public CartMapper(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    public CartResponse toResponse(Cart cart, List<CartItem> cartItems) {
        if (cart == null) return null;

        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(cartItemMapper::toResponse)
                .collect(Collectors.toList());

        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(
                cart.getId(),
                cart.getCustomer().getId(),
                itemResponses,
                totalAmount 
        );
    }
}