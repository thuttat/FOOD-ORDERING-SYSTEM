package duckie.example.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public record RestaurantAnalyticsResponse(
    
    BigDecimal totalRevenue,
    double revenueTrend,
    long totalOrders,
    double ordersTrend,
    BigDecimal averageOrderValue,
    double aovTrend,
    double cancellationRate,
    double cancellationTrend,

    
    List<DailyRevenueData> revenueOrderData,
    List<PeakHourData> peakHoursData,
    List<CategorySalesData> salesByCategory,
    List<TopMenuItemData> topMenuItems
) {
  
    public record DailyRevenueData(String date, BigDecimal revenue, Long orders) {}
    public record PeakHourData(String hour, Long orders) {}
    
    
    public record CategorySalesData(String name, Long value, BigDecimal amount, String color) {}
    public record TopMenuItemData(
        int rank, 
        String name, 
        String category, 
        String image, 
        String price, 
        int qtySold, 
        String revenue, 
        String trend,
        String trendLabel,
        List<Integer> sparklineValues 
    ) {}
}