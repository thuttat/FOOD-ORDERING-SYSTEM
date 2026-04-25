package duckie.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import duckie.example.backend.dto.ReviewRequest;
import duckie.example.backend.dto.ReviewResponse;
import duckie.example.backend.entity.User;
import duckie.example.backend.service.ReviewService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal User currentUser) {
        ReviewResponse response = reviewService.createReview(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/restaurants/{restaurantId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getRestaurantReviews(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(reviewService.getReviewsByRestaurant(restaurantId));
    }

    @GetMapping("/restaurants/{restaurantId}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(reviewService.getAverageRating(restaurantId));
    }
}