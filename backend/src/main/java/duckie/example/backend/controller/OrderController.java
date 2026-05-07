package duckie.example.backend.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import duckie.example.backend.dto.OrderRequest;
import duckie.example.backend.dto.OrderResponse;
import duckie.example.backend.entity.OrderStatus;
import duckie.example.backend.service.OrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/active")
    public ResponseEntity<List<OrderResponse>> getActiveOrders(Principal principal) {
        List<OrderResponse> activeOrders = orderService.getActiveOrders(principal.getName());
        return ResponseEntity.ok(activeOrders);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request,
            Principal principal) {
        OrderResponse response = orderService.createOrder(principal.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

    @GetMapping("/me")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Principal principal) {
        return ResponseEntity.ok(orderService.getMyOrderHistory(principal.getName()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(
            @PathVariable Long orderId,
            Principal principal) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId, principal.getName()));
    }

    @GetMapping("/history")
    public ResponseEntity<?> getOrderHistory(Authentication authentication) {
        try {
            String username = authentication.getName(); 
            List<OrderResponse> history = orderService.getMyOrderHistory(username);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error get order history: " + e.getMessage());
        }
    }
}