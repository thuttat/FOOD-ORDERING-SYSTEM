package duckie.example.backend.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import duckie.example.backend.dto.StatusChartData;
import duckie.example.backend.dto.TopRestaurantResponse;
import duckie.example.backend.entity.Order;
import duckie.example.backend.entity.OrderStatus;
import jakarta.persistence.Tuple;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items JOIN FETCH o.customer WHERE o.restaurant.id = :restaurantId AND o.status IN :statuses ORDER BY o.createdAt DESC")
    List<Order> findByRestaurantIdAndStatusInOrderByCreatedAtDesc(
            @Param("restaurantId") Long restaurantId,
            @Param("statuses") List<OrderStatus> statuses
    );

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items JOIN FETCH o.customer WHERE o.status IN :statuses ORDER BY o.createdAt DESC")
    List<Order> findByStatusInOrderByCreatedAtDesc(List<OrderStatus> statuses);

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByRestaurantId(Long restaurantId);

    @Query("SELECT SUM(o.totalAmount) FROM Order o " +
           "WHERE o.status IN ('DELIVERED', 'COMPLETED') AND month(o.createdAt) = month(current_date)")
    BigDecimal calculateRevenueThisMonth();

    @Query("SELECT new duckie.example.backend.dto.TopRestaurantResponse(" +
            "r.id, r.name, COUNT(DISTINCT o), SUM(o.totalAmount), CAST(COALESCE(AVG(rev.rating), 0.0) AS double)) " +
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
            "WHERE o.status IN ('DELIVERED', 'COMPLETED') AND FUNCTION('YEAR', o.createdAt) = FUNCTION('YEAR', CURRENT_DATE) " +
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

    List<Order> findByRestaurantIdAndStatus(Long resId, OrderStatus statuses);

    List<Order> findByRestaurantIdAndStatusIn(Long resId, List<OrderStatus> statuses);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.restaurant.id = :resId AND o.status = 'DELIVERED'")
    BigDecimal calculateTotalRevenue(@Param("resId") Long resId);

    long countByRestaurantIdAndStatus(Long restaurantId, OrderStatus status);

    @Query("SELECT function('DATE_FORMAT', o.createdAt, '%b %d'), SUM(o.totalAmount), COUNT(o) " +
            "FROM Order o WHERE o.restaurant.id = :resId GROUP BY function('DATE_FORMAT', o.createdAt, '%b %d')")
    List<Object[]> findDailyRevenueStats(@Param("resId") Long resId);

    @Query("SELECT function('DATE_FORMAT', o.createdAt, '%H:00'), COUNT(o) " +
            "FROM Order o WHERE o.restaurant.id = :resId GROUP BY function('DATE_FORMAT', o.createdAt, '%H:00')")
    List<Tuple> findPeakHourStats(@Param("resId") Long resId);

    @Query("SELECT c.name, COUNT(o), SUM(oi.unitPrice * oi.quantity) " +
            "FROM Order o JOIN o.items oi JOIN oi.menuItem mi JOIN mi.category c " +
            "WHERE o.restaurant.id = :resId AND o.status = 'DELIVERED' " +
            "GROUP BY c.name")
    List<Tuple> findSalesByCategory(@Param("resId") Long resId);

    @Query("SELECT mi.name, CAST(COUNT(oi) AS int), SUM(oi.unitPrice * oi.quantity), mi.imageUrl, mi.id, mi.price " +
        "FROM Order o JOIN o.items oi JOIN oi.menuItem mi " +
        "WHERE o.restaurant.id = :resId AND o.status = duckie.example.backend.entity.OrderStatus.DELIVERED " +
        "GROUP BY mi.name, mi.imageUrl, mi.id, mi.price " +
        "ORDER BY SUM(oi.unitPrice * oi.quantity) DESC")
    List<Object[]> findTopSellingItems(@Param("resId") Long resId);
    
    @Query("SELECT COALESCE(SUM(oi.unitPrice * oi.quantity), 0) " +
            "FROM Order o JOIN o.items oi " +
            "WHERE oi.menuItem.id = :itemId " +
            "AND o.status = duckie.example.backend.entity.OrderStatus.DELIVERED " +
            "AND o.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateItemRevenueInPeriod(
            @Param("itemId") Long itemId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);

    @Query("SELECT CAST(o.createdAt AS date), SUM(oi.unitPrice * oi.quantity) " +
            "FROM Order o JOIN o.items oi " +
            "WHERE oi.menuItem.id = :itemId " +
            "AND o.status = duckie.example.backend.entity.OrderStatus.DELIVERED " +
            "AND o.createdAt >= :startDate " +
            "GROUP BY CAST(o.createdAt AS date) " +
            "ORDER BY CAST(o.createdAt AS date) ASC")
    List<Object[]> findDailyRevenueByItem(
            @Param("itemId") Long itemId,
            @Param("startDate") Instant startDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.restaurant.id = :resId " +
            "AND o.status = duckie.example.backend.entity.OrderStatus.DELIVERED " +
            "AND o.createdAt BETWEEN :start AND :end")
    long countOrdersInPeriod(@Param("resId") Long resId, @Param("start") Instant start, @Param("end") Instant end);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.restaurant.id = :resId " +
            "AND o.status = duckie.example.backend.entity.OrderStatus.DELIVERED " +
            "AND o.createdAt BETWEEN :start AND :end")
    BigDecimal calculateRevenueInPeriod(@Param("resId") Long resId, @Param("start") Instant start, @Param("end") Instant end);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.restaurant.id = :resId " +
            "AND o.status = duckie.example.backend.entity.OrderStatus.CANCELLED " +
            "AND o.createdAt BETWEEN :start AND :end")
    long countCancelledOrdersInPeriod(@Param("resId") Long resId, @Param("start") Instant start, @Param("end") Instant end);

    @Query("SELECT new duckie.example.backend.dto.StatusChartData(o.status, COUNT(o)) " +
            "FROM Order o " +
            "WHERE FUNCTION('MONTH', o.createdAt) = FUNCTION('MONTH', CURRENT_DATE) " +
            "AND FUNCTION('YEAR', o.createdAt) = FUNCTION('YEAR', CURRENT_DATE) " +
            "GROUP BY o.status")
    List<StatusChartData> countOrderByStatusThisMonth();
    List<Order> findByRestaurantOwnerIdOrderByCreatedAtDesc(Long ownerId);
}