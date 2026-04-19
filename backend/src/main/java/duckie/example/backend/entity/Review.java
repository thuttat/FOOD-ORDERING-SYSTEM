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
@Table(name = "reviews")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    public Review() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }

    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public static ReviewBuilder builder() { return new ReviewBuilder(); }

    public static final class ReviewBuilder {
        private Long id;
        private User customer;
        private Restaurant restaurant;
        private Integer rating;
        private String comment;
        private Instant createdAt;
        private Instant updatedAt;

        public ReviewBuilder id(Long id) { this.id = id; return this; }
        public ReviewBuilder customer(User customer) { this.customer = customer; return this; }
        public ReviewBuilder restaurant(Restaurant restaurant) { this.restaurant = restaurant; return this; }
        public ReviewBuilder rating(Integer rating) { this.rating = rating; return this; }
        public ReviewBuilder comment(String comment) { this.comment = comment; return this; }
        public ReviewBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public ReviewBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public Review build() {
            Review review = new Review();
            review.setId(this.id);
            review.setCustomer(this.customer);
            review.setRestaurant(this.restaurant);
            review.setRating(this.rating);
            review.setComment(this.comment);
            review.setCreatedAt(this.createdAt);
            review.setUpdatedAt(this.updatedAt);
            return review;
        }
    }
}