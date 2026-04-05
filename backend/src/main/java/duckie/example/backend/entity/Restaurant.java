package duckie.example.backend.entity;

import java.time.Instant;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "restaurants", indexes = {
        @Index(name = "idx_restaurant_owner", columnList = "owner_id")
})
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column()
    private boolean isOpen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RestaurantStatus status;

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getOwner() { return user; }
    public void setOwner(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber;}

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean getIsOpen() { return isOpen; }
    public void setIsOpen(boolean isOpen) { this.isOpen = isOpen; }

    public RestaurantStatus getStatus() { return status; }
    public void setStatus(RestaurantStatus status) { this.status = status; }

    // --- Builder Pattern ---
    public static RestaurantBuilder builder() {
        return new RestaurantBuilder();
    }

    public static final class RestaurantBuilder {
        private Long id;
        private User user;
        private String name;
        private String phoneNumber;
        private String address;
        private String description;
        private String imageUrl;
        private boolean isOpen;
        private RestaurantStatus status;
        private Instant createdAt;
        private Instant updatedAt;

        public RestaurantBuilder id(Long id) { this.id = id; return this; }
        public RestaurantBuilder owner(User user) { this.user = user; return this; }
        public RestaurantBuilder name(String name) { this.name = name; return this; }
        public RestaurantBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public RestaurantBuilder address(String address) { this.address = address; return this; }
        public RestaurantBuilder description(String description) { this.description = description; return this; }
        public RestaurantBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public RestaurantBuilder isOpen(boolean isOpen) { this.isOpen = isOpen; return this; }
        public RestaurantBuilder status(RestaurantStatus status) { this.status = status; return this; }
        public RestaurantBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public RestaurantBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public Restaurant build() {
            Restaurant restaurant = new Restaurant();
            restaurant.setId(this.id);
            restaurant.setOwner(this.user);
            restaurant.setName(this.name);
            restaurant.setPhoneNumber(this.phoneNumber);
            restaurant.setAddress(this.address);
            restaurant.setDescription(this.description);
            restaurant.setImageUrl(this.imageUrl);
            restaurant.setIsOpen(this.isOpen);
            restaurant.setStatus(this.status);
            restaurant.setCreatedAt(this.createdAt);
            restaurant.setUpdatedAt(this.updatedAt);
            return restaurant;
        }
    }
}