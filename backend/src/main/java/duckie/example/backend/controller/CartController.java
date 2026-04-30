package duckie.example.backend.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import duckie.example.backend.dto.CartItemRequest;
import duckie.example.backend.dto.CartResponse;
import duckie.example.backend.entity.User;
import duckie.example.backend.service.CartService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(cartService.getCart(currentUser));
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItem(
            @Valid @RequestBody CartItemRequest request,
            Principal principal) {
        String username = principal.getName();
        cartService.addItemToCart(username, request); 
        return ResponseEntity.ok("Add cart successful!");
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @PathVariable Long cartItemId, 
            @RequestParam Integer quantity,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(cartService.updateCartItem(currentUser, cartItemId, quantity));
    }
}