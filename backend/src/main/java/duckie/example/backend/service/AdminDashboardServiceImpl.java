package duckie.example.backend.service;

import duckie.example.backend.dto.ChartData;
import duckie.example.backend.dto.DashboardStatsResponse;
import duckie.example.backend.dto.StatusChartData;
import duckie.example.backend.dto.TopRestaurantResponse;
import duckie.example.backend.repository.OrderRepository;
import duckie.example.backend.repository.RestaurantRepository;
import duckie.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {
    @Autowired private UserRepository userRepository;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private OrderRepository orderRepository;

    public DashboardStatsResponse getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalRestaurants = restaurantRepository.count();
        BigDecimal revenue = orderRepository.calculateRevenueThisMonth();
        List<Object[]> rawData = orderRepository.getRawMonthlyRevenueData();

        List<ChartData> revenueChart = rawData.stream().map(row -> {
            int monthIndex = Integer.parseInt(row[0].toString());
            String monthLabel = Month.of(monthIndex).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            BigDecimal amount = new BigDecimal(row[1].toString());

            return new ChartData(monthLabel, amount);
        }).collect(Collectors.toList());

        List<StatusChartData> statusChartData = orderRepository.countOrderByStatusThisMonth();
        List<TopRestaurantResponse> top5Restaurants = orderRepository.findTop5Restaurants(PageRequest.of(0, 5));
        long totalOrdersThisMonth = orderRepository.count();

        return new DashboardStatsResponse(totalUsers, totalRestaurants, totalOrdersThisMonth,
                revenue != null ? revenue : BigDecimal.ZERO, revenueChart, statusChartData, top5Restaurants);
    }
}
