package duckie.example.backend.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(nullable = false)
    private Integer quantity;

    public CartItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }

    public MenuItem getMenuItem() { return menuItem; }
    public void setMenuItem(MenuItem menuItem) { this.menuItem = menuItem; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public static CartItemBuilder builder() { return new CartItemBuilder(); }

    public static final class CartItemBuilder {
        private Long id;
        private Cart cart;
        private MenuItem menuItem;
        private Integer quantity;
        private Instant createdAt;
        private Instant updatedAt;

        public CartItemBuilder id(Long id) { this.id = id; return this; }
        public CartItemBuilder cart(Cart cart) { this.cart = cart; return this; }
        public CartItemBuilder menuItem(MenuItem menuItem) { this.menuItem = menuItem; return this; }
        public CartItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public CartItemBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public CartItemBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public CartItem build() {
            CartItem item = new CartItem();
            item.setId(this.id);
            item.setCart(this.cart);
            item.setMenuItem(this.menuItem);
            item.setQuantity(this.quantity);
            item.setCreatedAt(this.createdAt);
            item.setUpdatedAt(this.updatedAt);
            return item;
        }
    }
}