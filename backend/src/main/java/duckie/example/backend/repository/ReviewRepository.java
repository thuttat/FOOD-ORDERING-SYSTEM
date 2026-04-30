package duckie.example.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import duckie.example.backend.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);
    boolean existsByOrderId(Long orderId);
    List<Review> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.restaurant.id = :restaurantId")
    Double getAverageRatingByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("SELECT r FROM Review r WHERE " +
            "(:search IS NULL OR LOWER(r.customer.fullname) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(r.restaurant.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    org.springframework.data.domain.Page<Review> findAllForAdmin(@org.springframework.data.repository.query.Param("search") String search, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r")
    Double getSystemAverageRating();

    @Query("SELECT r.rating, COUNT(r) FROM Review r GROUP BY r.rating")
    java.util.List<Object[]> getRatingDistribution();

    @Query("SELECT FUNCTION('MONTH', r.createdAt), COUNT(r) " +
            "FROM Review r " +
            "WHERE FUNCTION('YEAR', r.createdAt) = FUNCTION('YEAR', CURRENT_DATE) " +
            "GROUP BY FUNCTION('MONTH', r.createdAt) " +
            "ORDER BY FUNCTION('MONTH', r.createdAt) ASC")
    List<Object[]> getMonthlyReviewCounts();
}