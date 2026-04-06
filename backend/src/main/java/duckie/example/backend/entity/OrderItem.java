package duckie.example.backend.entity;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items", indexes = {
    @Index(name = "idx_order_item_order", columnList = "order_id")
})
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    public OrderItem() {}

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public Order getOrder() { 
        return order; 
    }

    public void setOrder(Order order) { 
        this.order = order; 
    }

    public MenuItem getMenuItem() { 
        return menuItem; 
    }

    public void setMenuItem(MenuItem menuItem) { 
        this.menuItem = menuItem; 
    }

    public Integer getQuantity() { 
        return quantity; 
    }

    public void setQuantity(Integer quantity) { 
        this.quantity = quantity; 
    }

    public BigDecimal getUnitPrice() { 
        return unitPrice; 
    }

    public void setUnitPrice(BigDecimal unitPrice) { 
        this.unitPrice = unitPrice; 
    }

    public static OrderItemBuilder builder() { 
        return new OrderItemBuilder(); 
    }

    public static final class OrderItemBuilder {
        private Long id;
        private Order order;
        private MenuItem menuItem;
        private Integer quantity;
        private BigDecimal unitPrice;
        private Instant createdAt;
        private Instant updatedAt;

        public OrderItemBuilder id(Long id) { 
            this.id = id; 
            return this; 
        }

        public OrderItemBuilder order(Order order) { 
            this.order = order; 
            return this; 
        }

        public OrderItemBuilder menuItem(MenuItem menuItem) { 
            this.menuItem = menuItem; 
            return this; 
        }

        public OrderItemBuilder quantity(Integer quantity) { 
            this.quantity = quantity; 
            return this; 
        }

        public OrderItemBuilder unitPrice(BigDecimal unitPrice) { 
            this.unitPrice = unitPrice; 
            return this; 
        }

        public OrderItemBuilder createdAt(Instant createdAt) { 
            this.createdAt = createdAt; 
            return this; 
        }

        public OrderItemBuilder updatedAt(Instant updatedAt) { 
            this.updatedAt = updatedAt; 
            return this; 
        }

        public OrderItem build() {
            OrderItem item = new OrderItem();
            item.setId(this.id);
            item.setOrder(this.order);
            item.setMenuItem(this.menuItem);
            item.setQuantity(this.quantity);
            item.setUnitPrice(this.unitPrice);
            item.setCreatedAt(this.createdAt);
            item.setUpdatedAt(this.updatedAt);
            return item;
        }
    }
}