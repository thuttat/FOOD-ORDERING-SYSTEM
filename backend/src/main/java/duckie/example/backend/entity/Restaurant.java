package duckie.example.backend.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "restaurants")
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(length = 20)
    private String phone;

    @Column(name = "is_active")
    private Boolean isActive;

    public Restaurant() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public static RestaurantBuilder builder() {
        return new RestaurantBuilder();
    }

    public static final class RestaurantBuilder {
        private Long id;
        private String name;
        private String address;
        private String phone;
        private Boolean isActive;
        private Instant createdAt;
        private Instant updatedAt;

        public RestaurantBuilder id(Long id) {
            this.id = id;
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

        public RestaurantBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public RestaurantBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
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
            restaurant.setId(id);
            restaurant.setName(name);
            restaurant.setAddress(address);
            restaurant.setPhone(phone);
            restaurant.setIsActive(isActive);
            restaurant.setCreatedAt(createdAt);
            restaurant.setUpdatedAt(updatedAt);
            return restaurant;
        }
    }
}