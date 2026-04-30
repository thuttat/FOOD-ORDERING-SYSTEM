package duckie.example.backend.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import duckie.example.backend.entity.User;
import duckie.example.backend.repository.OrderRepository;
import duckie.example.backend.service.OrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
       
    }

    @GetMapping("/active")
    public ResponseEntity<List<OrderResponse>> getActiveOrders(Principal principal) {
        List<OrderResponse> activeOrders = orderService.getActiveOrders(principal.getName());
        return ResponseEntity.ok(activeOrders);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest orderRequest, 
            Principal principal) { 
        String username = principal.getName();
        OrderResponse response = orderService.createOrder(principal.getName(), orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(orderId, status);        
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderResponse>> getOrderHistory() {
        return ResponseEntity.ok(orderService.getOrderHistory());
    }

    

}