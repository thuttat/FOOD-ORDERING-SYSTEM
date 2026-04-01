package duckie.example.backend.entity;

import java.math.BigDecimal;
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
@Table(name = "menu_items", indexes = {
    @Index(name = "idx_menu_item_category", columnList = "category_id"),
    @Index(name = "idx_menu_item_name", columnList = "name")
})
public class MenuItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_available")
    private Boolean isAvailable;

    public MenuItem() {}

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public Category getCategory() { 
        return category; 
    }

    public void setCategory(Category category) { 
        this.category = category; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getDescription() { 
        return description; 
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public BigDecimal getPrice() { 
        return price; 
    }

    public void setPrice(BigDecimal price) { 
        this.price = price; 
    }

    public String getImageUrl() { 
        return imageUrl; 
    }

    public void setImageUrl(String imageUrl) { 
        this.imageUrl = imageUrl; 
    }

    public Boolean getIsAvailable() { 
        return isAvailable; 
    }

    public void setIsAvailable(Boolean isAvailable) { 
        this.isAvailable = isAvailable; 
    }

    public static MenuItemBuilder builder() { 
        return new MenuItemBuilder(); 
    }

    public static final class MenuItemBuilder {
        private Long id;
        private Category category;
        private String name;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private Boolean isAvailable;
        private Instant createdAt;
        private Instant updatedAt;

        public MenuItemBuilder id(Long id) { 
            this.id = id; 
            return this; 
        }

        public MenuItemBuilder category(Category category) { 
            this.category = category; 
            return this; 
        }

        public MenuItemBuilder name(String name) { 
            this.name = name; 
            return this; 
        }

        public MenuItemBuilder description(String description) { 
            this.description = description; 
            return this; 
        }

        public MenuItemBuilder price(BigDecimal price) { 
            this.price = price; 
            return this; 
        }

        public MenuItemBuilder imageUrl(String imageUrl) { 
            this.imageUrl = imageUrl; 
            return this; 
        }

        public MenuItemBuilder isAvailable(Boolean isAvailable) { 
            this.isAvailable = isAvailable; 
            return this; 
        }

        public MenuItemBuilder createdAt(Instant createdAt) { 
            this.createdAt = createdAt; 
            return this; 
        }

        public MenuItemBuilder updatedAt(Instant updatedAt) { 
            this.updatedAt = updatedAt; 
            return this; 
        }

        public MenuItem build() {
            MenuItem item = new MenuItem();
            item.setId(id);
            item.setCategory(category);
            item.setName(name);
            item.setDescription(description);
            item.setPrice(price);
            item.setImageUrl(imageUrl);
            item.setIsAvailable(isAvailable);
            item.setCreatedAt(createdAt);
            item.setUpdatedAt(updatedAt);
            return item;
        }
    }
}