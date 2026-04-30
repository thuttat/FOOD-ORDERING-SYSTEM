package duckie.example.backend.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import duckie.example.backend.dto.ChartData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import duckie.example.backend.dto.StatusChartData;
import duckie.example.backend.dto.TopRestaurantResponse;
import duckie.example.backend.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByRestaurantId(Long restaurantId);
    @Query("SELECT new duckie.example.backend.dto.StatusChartData(o.status, COUNT(o)) " +
           "FROM Order o WHERE month(o.createdAt) = month(current_date) " +
           "GROUP BY o.status")
    List<StatusChartData> countOrderByStatusThisMonth();

    @Query("SELECT SUM(o.totalAmount) FROM Order o " +
           "WHERE o.status = 'DELIVERED' AND month(o.createdAt) = month(current_date)")
    BigDecimal calculateRevenueThisMonth();

    @Query("SELECT new duckie.example.backend.dto.TopRestaurantResponse(" +
            "r.id, r.name, COUNT(DISTINCT o), SUM(o.totalAmount), COALESCE(AVG(rev.rating), 0.0)) " +
            "FROM Order o " +
            "JOIN o.restaurant r " +
            "LEFT JOIN Review rev ON rev.restaurant.id = r.id " +
            "WHERE month(o.createdAt) = month(current_date) " +
            "GROUP BY r.id, r.name " +
            "ORDER BY COUNT(DISTINCT o) DESC, SUM(o.totalAmount) DESC")
    List<TopRestaurantResponse> findTop5Restaurants(Pageable pageable);
    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    @Query("SELECT FUNCTION('MONTH', o.createdAt), SUM(o.totalAmount) " +
            "FROM Order o " +
            "WHERE o.status = 'DELIVERED' AND FUNCTION('YEAR', o.createdAt) = FUNCTION('YEAR', CURRENT_DATE) " +
            "GROUP BY FUNCTION('MONTH', o.createdAt) " +
            "ORDER BY FUNCTION('MONTH', o.createdAt) ASC")
    List<Object[]> getRawMonthlyRevenueData();

    @Query("SELECT o FROM Order o WHERE " +
            "(:search IS NULL OR CAST(o.id AS string) LIKE %:search% OR LOWER(o.customer.fullname) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:restaurantName IS NULL OR o.restaurant.name = :restaurantName)")
    Page<Order> findAdminOrders(
            @Param("search") String search,
            @Param("status") duckie.example.backend.entity.OrderStatus status,
            @Param("restaurantName") String restaurantName,
            Pageable pageable);

    @Query(value = "SELECT CAST(created_at AS DATE) as order_date, " +
            "SUM(CASE WHEN status = 'DELIVERED' THEN 1 ELSE 0 END) as completed, " +
            "SUM(CASE WHEN status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelled " +
            "FROM orders " +
            "WHERE created_at >= :startDate " +
            "GROUP BY CAST(created_at AS DATE) ORDER BY order_date ASC", nativeQuery = true)
    List<Object[]> getOrderStatsLast7Days(@Param("startDate") Instant startDate);
}