package duckie.example.backend.mapper;

import org.springframework.stereotype.Component;
import duckie.example.backend.dto.ReviewRequest;
import duckie.example.backend.dto.ReviewResponse;
import duckie.example.backend.entity.Order;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.Review;
import duckie.example.backend.entity.User;

import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequest request, User customer, Restaurant restaurant, Order order) {
        if (request == null) return null;

        return Review.builder()
                .customer(customer)
                .restaurant(restaurant)
                .order(order)
                .rating(request.rating())
                .comment(request.comment())
                .createdAt(Instant.now())
                .build();
    }

    public ReviewResponse toResponse(Review review) {
        if (review == null) return null;

        return new ReviewResponse(
                review.getId(),
                review.getRestaurant().getId(),
                review.getCustomer().getId(),
                review.getCustomer().getFullname(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}