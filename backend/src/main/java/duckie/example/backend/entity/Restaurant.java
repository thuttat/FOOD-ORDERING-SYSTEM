package duckie.example.backend.entity;

import java.time.Instant;

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
@Table(name = "restaurants", indexes = {
    @Index(name = "idx_restaurant_name", columnList = "name"),
    @Index(name = "idx_restaurant_owner", columnList = "user_id")
})
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 200)
    private String name;

    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_open")
    private Boolean isOpen;

    public Restaurant() {}

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public User getOwner() { 
        return owner; 
    }

    public void setOwner(User owner) { 
        this.owner = owner; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getAddress() { 
        return address; 
    }

    public void setAddress(String address) { 
        this.address = address; 
    }

    public String getDescription() { 
        return description; 
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public Boolean getIsOpen() { 
        return isOpen; 
    }

    public void setIsOpen(Boolean isOpen) { 
        this.isOpen = isOpen; 
    }

    public static RestaurantBuilder builder() { 
        return new RestaurantBuilder(); 
    }

    public static final class RestaurantBuilder {
        private Long id;
        private User owner;
        private String name;
        private String address;
        private String description;
        private Boolean isOpen;
        private Instant createdAt;
        private Instant updatedAt;

        public RestaurantBuilder id(Long id) { 
            this.id = id; 
            return this; 
        }

        public RestaurantBuilder owner(User owner) { 
            this.owner = owner; 
            return this; 
        }

        public RestaurantBuilder name(String name) { 
            this.name = name; 
            return this; 
        }

        public RestaurantBuilder address(String address) { 
            this.address = address; 
            return this; 
        }

        public RestaurantBuilder description(String description) { 
            this.description = description; 
            return this; 
        }

        public RestaurantBuilder isOpen(Boolean isOpen) { 
            this.isOpen = isOpen; 
            return this; 
        }

        public RestaurantBuilder createdAt(Instant createdAt) { 
            this.createdAt = createdAt; 
            return this; 
        }

        public RestaurantBuilder updatedAt(Instant updatedAt) { 
            this.updatedAt = updatedAt; 
            return this; 
        }

        public Restaurant build() {
            Restaurant restaurant = new Restaurant();
            restaurant.setId(this.id);
            restaurant.setOwner(this.owner);
            restaurant.setName(this.name);
            restaurant.setAddress(this.address);
            restaurant.setDescription(this.description);
            restaurant.setIsOpen(this.isOpen);
            restaurant.setCreatedAt(this.createdAt);
            restaurant.setUpdatedAt(this.updatedAt);
            return restaurant;
        }
    }
}