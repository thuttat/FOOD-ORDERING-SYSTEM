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
import java.util.List;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {
    @Autowired private UserRepository userRepository;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private OrderRepository orderRepository;

    public DashboardStatsResponse getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalRestaurants = restaurantRepository.count();
        BigDecimal revenue = orderRepository.calculateRevenueThisMonth();
        List<StatusChartData> statusChartData = orderRepository.countOrderByStatusThisMonth();
        List<TopRestaurantResponse> top5Restaurants = orderRepository.findTop5Restaurants(PageRequest.of(0, 5));

        List<ChartData> revenueChart = List.of(
                new ChartData("Tháng 12", new BigDecimal("5000000")),
                new ChartData("Tháng 1", new BigDecimal("7500000")),
                new ChartData("Tháng 2", new BigDecimal("6200000")),
                new ChartData("Tháng 3", new BigDecimal("9800000")),
                new ChartData("Tháng 4", revenue != null ? revenue : BigDecimal.ZERO)
        );

        return new DashboardStatsResponse(totalUsers, totalRestaurants, 150L,
                revenue, revenueChart, statusChartData, top5Restaurants);
    }
}
