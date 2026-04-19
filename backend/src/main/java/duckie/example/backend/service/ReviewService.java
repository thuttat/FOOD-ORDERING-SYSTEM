package duckie.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duckie.example.backend.dto.ReviewRequest;
import duckie.example.backend.dto.ReviewResponse;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.Review;
import duckie.example.backend.entity.User;
import duckie.example.backend.mapper.ReviewMapper;
import duckie.example.backend.repository.RestaurantRepository;
import duckie.example.backend.repository.ReviewRepository;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional
    public ReviewResponse createReview(ReviewRequest request, User customer) {
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
            .orElseThrow(() -> new RuntimeException("Nhà hàng không tồn tại!"));

        Review review = reviewMapper.toEntity(request, customer, restaurant);
        review = reviewRepository.save(review);
        return reviewMapper.toResponse(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByRestaurant(Long restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId).stream()
            .map(reviewMapper::toResponse)
            .collect(Collectors.toList());
    }
}