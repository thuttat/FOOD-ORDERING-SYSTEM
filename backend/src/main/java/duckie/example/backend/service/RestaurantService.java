package duckie.example.backend.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duckie.example.backend.dto.RestaurantAnalyticsResponse;
import duckie.example.backend.dto.RestaurantAnalyticsResponse.CategorySalesData;
import duckie.example.backend.dto.RestaurantAnalyticsResponse.DailyRevenueData;
import duckie.example.backend.dto.RestaurantAnalyticsResponse.PeakHourData;
import duckie.example.backend.dto.RestaurantAnalyticsResponse.TopMenuItemData;
import duckie.example.backend.dto.RestaurantRequest;
import duckie.example.backend.dto.RestaurantResponse;
import duckie.example.backend.entity.Order;
import duckie.example.backend.entity.OrderStatus;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.RestaurantStatus;
import duckie.example.backend.entity.Role;
import duckie.example.backend.entity.User;
import duckie.example.backend.exception.DuplicateResourceException;
import duckie.example.backend.exception.ResourceNotFoundException;
import duckie.example.backend.mapper.RestaurantMapper;
import duckie.example.backend.repository.OrderRepository;
import duckie.example.backend.repository.RestaurantRepository;
import duckie.example.backend.repository.UserRepository;
import io.jsonwebtoken.io.IOException;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RestaurantMapper restaurantMapper;
    private final OrderRepository orderRepository;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             UserRepository userRepository,
                             RestaurantMapper restaurantMapper,
                             OrderRepository orderRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.restaurantMapper = restaurantMapper;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> findAllActive() {
        return restaurantRepository.findByStatus(RestaurantStatus.ACTIVE).stream()
                .map(restaurant -> restaurantMapper.toResponse(restaurant, 0.0))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<RestaurantResponse> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return restaurantRepository.findAll(pageable)
                .map(restaurant -> restaurantMapper.toResponse(restaurant, 0.0));
    }

    @Transactional(readOnly = true)
    public Page<RestaurantResponse> getPendingRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return restaurantRepository.findByStatus(RestaurantStatus.PENDING, pageable)
                .map(restaurant -> restaurantMapper.toResponse(restaurant, 0.0));
    }

    @Transactional
    public RestaurantResponse createRestaurant(RestaurantRequest request, boolean isAdminCreated) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (restaurantRepository.findByOwnerId(currentUser.getId()).isPresent()) {
            throw new DuplicateResourceException("User already has a restaurant!");
        }

        Restaurant newRestaurant = restaurantMapper.toEntity(request, currentUser);
        newRestaurant.setIsOpen(false);

        if (isAdminCreated) {
            newRestaurant.setStatus(RestaurantStatus.ACTIVE);
            if (currentUser.getRole() == Role.USER) {
                currentUser.setRole(Role.RESTAURANT);
                userRepository.save(currentUser);
            }
        } else {
            newRestaurant.setStatus(RestaurantStatus.PENDING);
        }
        return restaurantMapper.toResponse(restaurantRepository.save(newRestaurant), 0.0);
    }

    @Transactional
    public RestaurantResponse approveRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + id));
        restaurant.setStatus(RestaurantStatus.ACTIVE);

        User owner = restaurant.getOwner();
        if (owner.getRole() == Role.USER) {
            owner.setRole(Role.RESTAURANT);
            userRepository.save(owner);
        }

        return restaurantMapper.toResponse(restaurantRepository.save(restaurant), 0.0);
    }

    @Transactional
    public RestaurantResponse lockRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + id));

        restaurant.setStatus(RestaurantStatus.LOCKED);
        return restaurantMapper.toResponse(restaurantRepository.save(restaurant), 0.0);
    }

    @Transactional(readOnly = true)
    public RestaurantAnalyticsResponse getAnalytics(String username, String timeFilter) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Long resId = restaurant.getId();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentStart;
        LocalDateTime previousStart;

        if ("Last 30 Days".equals(timeFilter)) {
            currentStart = now.minusDays(30);
            previousStart = currentStart.minusDays(30); 
        } else if ("This Month".equals(timeFilter)) {
            currentStart = now.withDayOfMonth(1).withHour(0).withMinute(0); 
            previousStart = currentStart.minusMonths(1); 
        } else {
            currentStart = now.minusDays(7);
            previousStart = currentStart.minusDays(7);
        }
      
        java.time.Instant nowInstant = now.atZone(ZoneId.systemDefault()).toInstant();
        java.time.Instant currentStartInstant = currentStart.atZone(ZoneId.systemDefault()).toInstant();
        java.time.Instant prevStartInstant = previousStart.atZone(ZoneId.systemDefault()).toInstant();

       
        long totalOrders = orderRepository.countOrdersInPeriod(resId, currentStartInstant, nowInstant);
        BigDecimal totalRevenue = orderRepository.calculateRevenueInPeriod(resId, currentStartInstant, nowInstant);
        BigDecimal avgOrderValue = totalOrders > 0 
                ? totalRevenue.divide(BigDecimal.valueOf(totalOrders), 0, RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;

        
        BigDecimal revLastPeriod = orderRepository.calculateRevenueInPeriod(resId, prevStartInstant, currentStartInstant);
        double revenueTrend = calculatePercentageChange(revLastPeriod, totalRevenue);

        long ordersLastPeriod = orderRepository.countOrdersInPeriod(resId, prevStartInstant, currentStartInstant);
        double ordersTrend = calculatePercentageChange(BigDecimal.valueOf(ordersLastPeriod), BigDecimal.valueOf(totalOrders));

        BigDecimal aovLastPeriod = ordersLastPeriod > 0 
                ? revLastPeriod.divide(BigDecimal.valueOf(ordersLastPeriod), 2, RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;
        double aovTrend = calculatePercentageChange(aovLastPeriod, avgOrderValue);

        long cancelledOrders = orderRepository.countByRestaurantIdAndStatus(resId, OrderStatus.CANCELLED);
        double cancellationRate = (totalOrders + cancelledOrders) > 0 
                ? (double) cancelledOrders / (totalOrders + cancelledOrders) * 100 
                : 0;
        cancellationRate = Math.round(cancellationRate * 10.0) / 10.0;
        

       
        List<Order> completedOrders = orderRepository.findByRestaurantIdAndStatus(resId, OrderStatus.DELIVERED);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd").withZone(ZoneId.systemDefault());
        Map<String, List<Order>> ordersByDate = completedOrders.stream()
                .collect(Collectors.groupingBy(o -> dateFormatter.format(o.getCreatedAt())));

        List<DailyRevenueData> revenueOrderData = ordersByDate.entrySet().stream()
                .map(entry -> {
                    BigDecimal rev = entry.getValue().stream()
                            .map(Order::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new DailyRevenueData(entry.getKey(), rev, (long) entry.getValue().size());
                })
                .collect(Collectors.toList());

        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:00").withZone(ZoneId.systemDefault());
        Map<String, Long> ordersByHour = completedOrders.stream()
                .collect(Collectors.groupingBy(o -> hourFormatter.format(o.getCreatedAt()), Collectors.counting()));

        List<PeakHourData> peakHoursData = ordersByHour.entrySet().stream()
                .map(entry -> new PeakHourData(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        
        List<Object[]> categoryRaw = orderRepository.findSalesByCategory(resId);
        String[] colors = {"#10B981", "#3B82F6", "#F59E0B", "#EC4899", "#8B5CF6"};
        List<CategorySalesData> salesByCategory = new ArrayList<>();
        for (int i = 0; i < categoryRaw.size(); i++) {
            Object[] row = categoryRaw.get(i);
            salesByCategory.add(new CategorySalesData(
                    row[0].toString(),
                    Integer.parseInt(row[1].toString()),
                    new BigDecimal(row[2].toString()),
                    colors[i % colors.length]
            ));
        }

        
        List<Object[]> topItemsRaw = orderRepository.findTopSellingItems(resId);
        List<TopMenuItemData> topSellingItems = topItemsRaw.stream()
            .limit(5)
            .map(row -> {
                Long itemId = Long.valueOf(row[4].toString()); 

               
                BigDecimal itemRevThisWeek = orderRepository.calculateItemRevenueInPeriod(itemId, currentStartInstant, nowInstant);
                BigDecimal itemRevLastWeek = orderRepository.calculateItemRevenueInPeriod(itemId, prevStartInstant, currentStartInstant);
                
                double itemGrowth = calculatePercentageChange(itemRevLastWeek, itemRevThisWeek);
                String trendStatus = (itemGrowth >= 0) ? "up" : "down";

                String calculatedTrendLabel = String.format("%.1f%%", Math.abs(itemGrowth));

                
                List<Object[]> sparkRaw = orderRepository.findDailyRevenueByItem(itemId, currentStartInstant);
                List<Integer> sparkValues = sparkRaw.stream()
                        .map(r -> ((BigDecimal) r[1]).intValue())
                        .collect(Collectors.toList());
                while (sparkValues.size() < 7) { sparkValues.add(0, 0); }

                return new TopMenuItemData(
                    topItemsRaw.indexOf(row) + 1,
                    row[0].toString(),
                    "Main Course", 
                    row[3] != null ? row[3].toString() : "",
                    row[5].toString() + " VND",
                    Integer.parseInt(row[1].toString()),
                    row[2].toString() + " VND",
                    trendStatus,
                    calculatedTrendLabel,
                    sparkValues
                );
            })
            .collect(Collectors.toList());

        return new RestaurantAnalyticsResponse(
            totalRevenue, revenueTrend,
            totalOrders, ordersTrend,
            avgOrderValue, aovTrend,
            cancellationRate, -0.5, 
            revenueOrderData,
            peakHoursData,
            salesByCategory,
            topSellingItems
        );
    }

    private double calculatePercentageChange(BigDecimal previous, BigDecimal current) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return (current != null && current.compareTo(BigDecimal.ZERO) > 0) ? 100.0 : 0.0;
        }
        return current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }


    @Transactional(readOnly = true)
    public byte[] exportRestaurantReport(String username) throws java.io.IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Long resId = restaurant.getId();

        List<Order> completedOrders = orderRepository.findByRestaurantIdAndStatus(resId, OrderStatus.DELIVERED);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Analytics Report");

            Row headerRow = sheet.createRow(0);
            String[] columns = {"Order ID", "Customer", "Amount (VND)", "Date", "Status"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowIdx = 1;
            for (Order order : completedOrders) {
                Row row = sheet.createRow(rowIdx++);
                
                row.createCell(0).setCellValue(order.getId());
                
                String customerName = order.getCustomer() != null ? order.getCustomer().getFullname() : "Unknown Customer";
                row.createCell(1).setCellValue(customerName);
                
                double amount = order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0.0;
                row.createCell(2).setCellValue(amount);
                
                String dateStr = order.getCreatedAt() != null ? order.getCreatedAt().toString() : "";
                row.createCell(3).setCellValue(dateStr);
        
                String statusStr = order.getStatus() != null ? order.getStatus().toString() : "";
                row.createCell(4).setCellValue(statusStr);
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Fail to export Excel: " + e.getMessage());
        }
    }

}
