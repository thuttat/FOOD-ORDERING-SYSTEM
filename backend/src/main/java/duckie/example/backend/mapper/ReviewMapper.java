package duckie.example.backend.mapper;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.ReviewRequest;
import duckie.example.backend.dto.ReviewResponse;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.Review;
import duckie.example.backend.entity.User;

@Component
public class ReviewMapper {
    public Review toEntity(ReviewRequest request, User customer, Restaurant restaurant) {
        if (request == null) return null;
        return Review.builder()
                .customer(customer)
                .restaurant(restaurant)
                .rating(request.rating())
                .comment(request.comment())
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