package duckie.example.backend.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Transactional(readOnly = true)
    public Page<ReviewResponse> getAllReviewsForAdmin(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewRepository.findAllForAdmin(search, pageable).map(reviewMapper::toResponse);
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getReviewStats() {
        long totalReviews = reviewRepository.count();
        Double avgRating = reviewRepository.getSystemAverageRating();

        List<Object[]> distRaw = reviewRepository.getRatingDistribution();
        long[] starCounts = new long[5];

        for (Object[] row : distRaw) {
            if (row[0] != null && row[1] != null) {
                int rating = ((Number) row[0]).intValue();
                long count = ((Number) row[1]).longValue();
                if (rating >= 1 && rating <= 5) starCounts[rating - 1] = count;
            }
        }

        List<Map<String, Object>> distributionList = List.of(
                Map.of("rating", "5 Stars", "count", starCounts[4], "color", "#10b981"),
                Map.of("rating", "4 Stars", "count", starCounts[3], "color", "#3b82f6"),
                Map.of("rating", "3 Stars", "count", starCounts[2], "color", "#f59e0b"),
                Map.of("rating", "2 Stars", "count", starCounts[1], "color", "#ff6b35"),
                Map.of("rating", "1 Star", "count", starCounts[0], "color", "#ef4444")
        );

        List<Object[]> activityRaw = reviewRepository.getMonthlyReviewCounts();
        String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        List<Map<String, Object>> activityList = new java.util.ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            Map<String, Object> monthData = new java.util.HashMap<>();
            monthData.put("month", months[i]);
            monthData.put("count", 0L);
            activityList.add(monthData);
        }

        for (Object[] row : activityRaw) {
            int monthIndex = ((Number) row[0]).intValue();
            activityList.get(monthIndex - 1).put("count", ((Number) row[1]).longValue());
        }

        return Map.of(
                "totalReviews", totalReviews,
                "averageRating", avgRating != null ? String.format("%.1f", avgRating) : "0.0",
                "ratingDistribution", distributionList,
                "reviewActivity", activityList
        );
    }
}