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
@Table(name = "categories", indexes = {
    @Index(name = "idx_category_restaurant", columnList = "restaurant_id")
})
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false, length = 100)
    private String name;

    public Category() {}

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public Restaurant getRestaurant() { 
        return restaurant; 
    }

    public void setRestaurant(Restaurant restaurant) { 
        this.restaurant = restaurant; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public static CategoryBuilder builder() { 
        return new CategoryBuilder(); 
    }

    public static final class CategoryBuilder {
        private Long id;
        private Restaurant restaurant;
        private String name;
        private Instant createdAt;
        private Instant updatedAt;

        public CategoryBuilder id(Long id) { 
            this.id = id; 
            return this; 
        }

        public CategoryBuilder restaurant(Restaurant restaurant) { 
            this.restaurant = restaurant; 
            return this; 
        }

        public CategoryBuilder name(String name) { 
            this.name = name; 
            return this; 
        }

        public CategoryBuilder createdAt(Instant createdAt) { 
            this.createdAt = createdAt; 
            return this; 
        }

        public CategoryBuilder updatedAt(Instant updatedAt) { 
            this.updatedAt = updatedAt; 
            return this; 
        }

        public Category build() {
            Category category = new Category();
            category.setId(this.id);
            category.setRestaurant(this.restaurant);
            category.setName(this.name);
            category.setCreatedAt(this.createdAt);
            category.setUpdatedAt(this.updatedAt);
            return category;
        }
    }
}