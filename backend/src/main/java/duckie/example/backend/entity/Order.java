package duckie.example.backend.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_orders_customer", columnList = "customer_id"),
    @Index(name = "idx_orders_restaurant", columnList = "restaurant_id")
})
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "delivery_fee")
    private BigDecimal deliveryFee;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(columnDefinition = "TEXT")
    private String customerNote;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

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

    public Restaurant getRestaurant() { 
        return restaurant; 
    }

    public void setRestaurant(Restaurant restaurant) { 
        this.restaurant = restaurant; 
    }

    public BigDecimal getTotalAmount() { 
        return totalAmount; 
    }

    public void setTotalAmount(BigDecimal totalAmount) { 
        this.totalAmount = totalAmount; 
    }

    public OrderStatus getStatus() { 
        return status; 
    }

    public void setStatus(OrderStatus status) { 
        this.status = status; 
    }

    public String getDeliveryAddress() { 
        return deliveryAddress; 
    }

    public void setDeliveryAddress(String deliveryAddress) { 
        this.deliveryAddress = deliveryAddress; 
    }

    public List<OrderItem> getItems() { 
        return items; 
    }

    public void setItems(List<OrderItem> items) { 
        this.items = items; 
    }

    public static OrderBuilder builder() { 
        return new OrderBuilder(); 
    }

    public static final class OrderBuilder {
        private Long id;
        private User customer;
        private Restaurant restaurant;
        private BigDecimal totalAmount;
        private OrderStatus status;
        private String deliveryAddress;
        private List<OrderItem> items;
        private Instant createdAt;
        private Instant updatedAt;

        public OrderBuilder id(Long id) { 
            this.id = id; 
            return this; 
        }

        public OrderBuilder customer(User customer) { 
            this.customer = customer; 
            return this; 
        }

        public OrderBuilder restaurant(Restaurant restaurant) { 
            this.restaurant = restaurant; 
            return this; 
        }

        public OrderBuilder totalAmount(BigDecimal totalAmount) { 
            this.totalAmount = totalAmount; 
            return this; 
        }

        public OrderBuilder status(OrderStatus status) { 
            this.status = status; 
            return this; 
        }

        public OrderBuilder deliveryAddress(String deliveryAddress) { 
            this.deliveryAddress = deliveryAddress; 
            return this; 
        }

        public OrderBuilder items(List<OrderItem> items) { 
            this.items = items; 
            return this; 
        }

        public OrderBuilder createdAt(Instant createdAt) { 
            this.createdAt = createdAt; 
            return this; 
        }

        public OrderBuilder updatedAt(Instant updatedAt) { 
            this.updatedAt = updatedAt; 
            return this; 
        }

        public Order build() {
            Order order = new Order();
            order.setId(this.id);
            order.setCustomer(this.customer);
            order.setRestaurant(this.restaurant);
            order.setTotalAmount(this.totalAmount);
            order.setStatus(this.status);
            order.setDeliveryAddress(this.deliveryAddress);
            order.setItems(this.items);
            order.setCreatedAt(this.createdAt);
            order.setUpdatedAt(this.updatedAt);
            return order;
        }
    }
}