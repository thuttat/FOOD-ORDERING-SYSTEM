package duckie.example.backend.repository;

import duckie.example.backend.dto.StatusChartData;
import duckie.example.backend.dto.TopRestaurantResponse;
import duckie.example.backend.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT new duckie.example.backend.dto.StatusChartData(o.status, COUNT(o)) " +
        "FROM Order o WHERE month(o.createdAt) = month(current_date) " +
        "GROUP BY o.status")
    List<StatusChartData> countOrderByStatusThisMonth();

    @Query("SELECT SUM(o.totalAmount) FROM Order o " +
            "WHERE o.status = 'DELIVERED' AND month(o.createdAt) = month(current_date)")
    BigDecimal calculateRevenueThisMonth();

    @Query("SELECT new duckie.example.backend.dto.TopRestaurantResponse(" +
            "r.id, r.name, COUNT(o), SUM(o.totalAmount), 5.0) " +
            "FROM Order o JOIN o.restaurant r " +
            "WHERE month(o.createdAt) = month(current_date) " +
            "GROUP BY r.id, r.name " +
            "ORDER BY COUNT(o) DESC, SUM(o.totalAmount) DESC")
    List<TopRestaurantResponse> findTop5Restaurants(Pageable pageable);
}
