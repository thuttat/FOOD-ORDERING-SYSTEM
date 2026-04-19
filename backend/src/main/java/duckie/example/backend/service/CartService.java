package duckie.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duckie.example.backend.dto.CartItemRequest;
import duckie.example.backend.dto.CartResponse;
import duckie.example.backend.entity.Cart;
import duckie.example.backend.entity.CartItem;
import duckie.example.backend.entity.MenuItem;
import duckie.example.backend.entity.User;
import duckie.example.backend.mapper.CartMapper;
import duckie.example.backend.repository.CartItemRepository;
import duckie.example.backend.repository.CartRepository;
import duckie.example.backend.repository.MenuItemRepository;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, 
                       MenuItemRepository menuItemRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.menuItemRepository = menuItemRepository;
        this.cartMapper = cartMapper;
    }

    @Transactional(readOnly = true)
    public CartResponse getCart(User customer) {
        Cart cart = cartRepository.findByCustomerId(customer.getId()).orElseGet(() -> {
            Cart newCart = Cart.builder().customer(customer).build();
            return cartRepository.save(newCart);
        });
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        return cartMapper.toResponse(cart, items);
    }

    @Transactional
    public CartResponse addItemToCart(User customer, CartItemRequest request) {
        Cart cart = cartRepository.findByCustomerId(customer.getId()).orElseGet(() -> 
            cartRepository.save(Cart.builder().customer(customer).build())
        );

        MenuItem menuItem = menuItemRepository.findById(request.menuItemId())
            .orElseThrow(() -> new RuntimeException("Món ăn không tồn tại!"));

        CartItem cartItem = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), menuItem.getId())
            .orElse(CartItem.builder().cart(cart).menuItem(menuItem).quantity(0).build());

        cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
        cartItemRepository.save(cartItem);

        return getCart(customer);
    }

    @Transactional
    public CartResponse updateCartItem(User customer, Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new RuntimeException("Chi tiết giỏ hàng không tồn tại!"));
        
        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
        return getCart(customer);
    }

    @Transactional
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}