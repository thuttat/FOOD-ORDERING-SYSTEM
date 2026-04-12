package duckie.example.backend.mapper;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.OrderItemResponse;
import duckie.example.backend.entity.CartItem;
import duckie.example.backend.entity.Order;
import duckie.example.backend.entity.OrderItem;

@Component
public class OrderItemMapper {

    public OrderItem fromCartItem(CartItem cartItem, Order order) {
        if (cartItem == null) return null;

        return OrderItem.builder()
                .order(order)
                .menuItem(cartItem.getMenuItem())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getMenuItem().getPrice()) 
                .build();
    }

    public OrderItemResponse toResponse(OrderItem item) {
        if (item == null) return null;

        return new OrderItemResponse(
            item.getId(),
            item.getMenuItem().getId(),
            item.getMenuItem().getName(),
            item.getQuantity(),
            item.getUnitPrice()
        );
    }
}