package duckie.example.backend.dto;

import java.math.BigDecimal;
import java.util.List;


public record DashboardStatsResponse(
        long totalActiveUsers,
        long totalActiveRestaurants,
        long totalOrdersThisMonth,
        BigDecimal revenueThisMonth,
        List<ChartData> monthlyRevenueChart,
        List<StatusChartData> orderStatusChart,
        List<TopRestaurantResponse> topRestaurants
) {}
