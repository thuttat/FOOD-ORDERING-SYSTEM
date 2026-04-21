package duckie.example.backend.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    public Cart() {}

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public User getCustomer() { 
        return customer; 
    }
    public void setCustomer(User customer) { 
        this.customer = customer; 
    }

    public static CartBuilder builder() { 
        return new CartBuilder(); 
    }

    public static final class CartBuilder {
        private Long id;
        private User customer;
        private Instant createdAt;
        private Instant updatedAt;

        public CartBuilder id(Long id) { 
            this.id = id; 
            return this; 
        }
        public CartBuilder customer(User customer) { 
            this.customer = customer; 
            return this; 
        }
        public CartBuilder createdAt(Instant createdAt) { 
            this.createdAt = createdAt; 
            return this; 
        }
        public CartBuilder updatedAt(Instant updatedAt) { 
            this.updatedAt = updatedAt; 
            return this; 
        }

        public Cart build() {
            Cart cart = new Cart();
            cart.setId(this.id);
            cart.setCustomer(this.customer);
            cart.setCreatedAt(this.createdAt);
            cart.setUpdatedAt(this.updatedAt);
            return cart;
        }
    }
}