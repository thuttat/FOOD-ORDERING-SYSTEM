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
}