package duckie.example.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import duckie.example.backend.dto.OrderRequest;
import duckie.example.backend.dto.OrderResponse;
import duckie.example.backend.entity.OrderStatus;
import duckie.example.backend.entity.User;
import duckie.example.backend.service.OrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request, 
            @AuthenticationPrincipal User currentUser) {

        OrderResponse response = orderService.createOrder(currentUser, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        
        OrderResponse updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponse>> getAdminOrders(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String restaurantName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(orderService.getAdminOrders(search, status, restaurantName, page, size));
    }

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAdminOrderStats() {
        return ResponseEntity.ok(orderService.getAdminOrderStats());
    }
}