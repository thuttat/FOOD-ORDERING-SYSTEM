package duckie.example.backend.entity;

import java.time.Instant;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "restaurants")
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_open")
    private Boolean isOpen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantStatus status;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Category> categories;

    public Restaurant() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getphone() { return phone; }
    public void setphone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getIsOpen() { return isOpen; }
    public void setIsOpen(Boolean isOpen) { this.isOpen = isOpen; }

    public RestaurantStatus getStatus() { return status; }
    public void setStatus(RestaurantStatus status) { this.status = status; }

    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }

    public static RestaurantBuilder builder() { return new RestaurantBuilder(); }

    public static final class RestaurantBuilder {
        private Long id;
        private User owner;
        private String name;
        private String phone;
        private String address;
        private String description;
        private String imageUrl;
        private Boolean isOpen;
        private RestaurantStatus status;
        private Instant createdAt;
        private Instant updatedAt;

        public RestaurantBuilder id(Long id) { this.id = id; return this; }
        public RestaurantBuilder owner(User owner) { this.owner = owner; return this; }
        public RestaurantBuilder name(String name) { this.name = name; return this; }
        public RestaurantBuilder phone(String phone) { this.phone = phone; return this; }
        public RestaurantBuilder address(String address) { this.address = address; return this; }
        public RestaurantBuilder description(String description) { this.description = description; return this; }
        public RestaurantBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public RestaurantBuilder isOpen(Boolean isOpen) { this.isOpen = isOpen; return this; }
        public RestaurantBuilder status(RestaurantStatus status) { this.status = status; return this; }
        public RestaurantBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public RestaurantBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public Restaurant build() {
            Restaurant restaurant = new Restaurant();
            restaurant.setId(this.id);
            restaurant.setOwner(this.owner);
            restaurant.setName(this.name);
            restaurant.setphone(this.phone);
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