package duckie.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duckie.example.backend.dto.ReviewRequest;
import duckie.example.backend.dto.ReviewResponse;
import duckie.example.backend.entity.Order;
import duckie.example.backend.entity.OrderStatus;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.Review;
import duckie.example.backend.entity.User;
import duckie.example.backend.mapper.ReviewMapper;
import duckie.example.backend.repository.OrderRepository;
import duckie.example.backend.repository.RestaurantRepository;
import duckie.example.backend.repository.ReviewRepository;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository,
                         RestaurantRepository restaurantRepository,
                         OrderRepository orderRepository,
                         ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderRepository = orderRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional
    public ReviewResponse createReview(ReviewRequest request, User customer) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Cannot find!"));
        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("It's not your bill!");
        }

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new RuntimeException("Pls rating after finish your meal!");
        }

        if (reviewRepository.existsByOrderId(request.orderId())) {
            throw new RuntimeException("You rated this one!");
        }
        Restaurant restaurant = order.getRestaurant();
        Review review = reviewMapper.toEntity(request, customer, restaurant, order);
        review = reviewRepository.save(review);

        return reviewMapper.toResponse(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByRestaurant(Long restaurantId) {
        return reviewRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId).stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Double getAverageRating(Long restaurantId) {
        return reviewRepository.getAverageRatingByRestaurantId(restaurantId);
    }
}