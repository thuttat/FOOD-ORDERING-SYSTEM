package duckie.example.backend.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.OrderItemResponse;
import duckie.example.backend.dto.OrderRequest;
import duckie.example.backend.dto.OrderResponse;
import duckie.example.backend.entity.Order;
import duckie.example.backend.entity.OrderStatus;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.User;

@Component
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public OrderMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    public Order toEntity(OrderRequest request, User customer, Restaurant restaurant) {
        if (request == null) return null;

        return Order.builder()
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(request.deliveryAddress())
                .customerNote(request.customerNote())
                .status(OrderStatus.PENDING)
                .deliveryFee(new BigDecimal("15000")) 
                .totalAmount(BigDecimal.ZERO) 
                .items(new ArrayList<>())
                .build();
    }

    public OrderResponse toResponse(Order order) {
        if (order == null) return null;
        List<OrderItemResponse> itemDtos = order.getItems() != null 
            ? order.getItems().stream()
                .map(orderItemMapper::toResponse)
                .collect(Collectors.toList())
            : new ArrayList<>();

        return new OrderResponse(
            order.getId(),
            order.getCustomer() != null ? order.getCustomer().getId() : null,
            order.getCustomer() != null ? order.getCustomer().getFullname() : "N/A", 
            order.getCustomer() != null ? order.getCustomer().getPhone() : "N/A",
            order.getRestaurant() != null ? order.getRestaurant().getId() : null,
            order.getRestaurant() != null ? order.getRestaurant().getName() : "N/A",     
            order.getTotalAmount(),
            order.getDeliveryFee(),
            order.getStatus() != null ? order.getStatus().name() : "PENDING",
            order.getDeliveryAddress(),
            order.getCustomerNote(),
            itemDtos,
            order.getCreatedAt()
        );
    }
}