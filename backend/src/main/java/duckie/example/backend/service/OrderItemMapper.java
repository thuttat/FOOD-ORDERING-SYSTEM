package duckie.example.backend.service;

import org.springframework.stereotype.Component;
import duckie.example.backend.dto.OrderItemRequest;
import duckie.example.backend.dto.OrderItemResponse;
import duckie.example.backend.entity.Order;
import duckie.example.backend.entity.OrderItem;
import duckie.example.backend.entity.MenuItem;

@Component
public class OrderItemMapper {
    public OrderItem toEntity(OrderItemRequest request, Order order, MenuItem menuItem) {
        if (request == null) {
            return null;
        }

        return OrderItem.builder()
                .order(order)
                .menuItem(menuItem)
                .quantity(request.quantity())
                .unitPrice(menuItem.getPrice()) 
                .build();
    }

    public OrderItemResponse toResponse(OrderItem item) {
        if (item == null) {
            return null;
        }

        return new OrderItemResponse(
            item.getId(),
            item.getMenuItem().getId(),
            item.getMenuItem().getName(),
            item.getQuantity(),
            item.getUnitPrice()
        );
    }
}