package duckie.example.backend.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException; 
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import duckie.example.backend.config.RabbitMQConfig;
import duckie.example.backend.dto.RestaurantAnalyticsResponse;
import duckie.example.backend.dto.RestaurantAnalyticsResponse.DailyRevenueData;
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
import jakarta.persistence.Tuple;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RestaurantMapper restaurantMapper;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;


    public RestaurantService(RestaurantRepository restaurantRepository,
                             UserRepository userRepository,
                             RestaurantMapper restaurantMapper,
                             OrderRepository orderRepository,
                             RabbitTemplate rabbitTemplate,
                             ObjectMapper objectMapper) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.restaurantMapper = restaurantMapper;
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    private void sendEmailEvent(String to, String subject, String body) {
        try {
            Map<String, String> emailData = Map.of("to", to, "subject", subject, "body", body);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_APP, RabbitMQConfig.ROUTING_KEY_EMAIL, objectMapper.writeValueAsString(emailData));
        } catch (Exception e) {
            System.err.println("RabbitMQ Email Error: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> findAllActive() {
        return restaurantRepository.findByIsOpenTrueAndStatus(RestaurantStatus.ACTIVE).stream()
                .map(r -> restaurantMapper.toResponse(r, 5.0))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<RestaurantResponse> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return restaurantRepository.findAll(pageable)
                .map(restaurant -> restaurantMapper.toResponse(restaurant, 0.0));
    }

    @Transactional(readOnly = true)
    public Page<RestaurantResponse> getPendingRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return restaurantRepository.findByStatus(RestaurantStatus.PENDING, pageable)
                .map(restaurant -> restaurantMapper.toResponse(restaurant, 0.0));
    }

    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        return restaurantMapper.toResponse(restaurant, 0.0);
    }

    @Transactional
    public RestaurantResponse createRestaurant(RestaurantRequest request, boolean isAdminCreated) {
        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + request.ownerId()));

        if (restaurantRepository.findByOwnerId(owner.getId()).isPresent()) {
            throw new DuplicateResourceException("User already has a restaurant!");
        }

        Restaurant newRestaurant = restaurantMapper.toEntity(request, owner);
        newRestaurant.setIsOpen(false);
        newRestaurant.setStatus(isAdminCreated ? RestaurantStatus.ACTIVE : RestaurantStatus.PENDING);

        if (isAdminCreated && owner.getRole() == Role.USER) {
            owner.setRole(Role.RESTAURANT);
            userRepository.save(owner);
        }

        Restaurant saved = restaurantRepository.save(newRestaurant);

        if (!isAdminCreated) {
            try {
                Map<String, String> msg = Map.of(
                        "message", "New restaurant registration: " + saved.getName() + " is awaiting approval!",
                        "type", "RESTAURANT_PENDING"
                );
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_APP, RabbitMQConfig.ROUTING_KEY_ADMIN, objectMapper.writeValueAsString(msg));
            } catch (Exception e) { e.printStackTrace(); }
        }

        return restaurantMapper.toResponse(saved, 0.0);
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

        Restaurant saved = restaurantRepository.save(restaurant);
        sendEmailEvent(saved.getOwner().getEmail(), "Congratulations!",
                "Your restaurant " + saved.getName() + " has been approved.");

        return restaurantMapper.toResponse(saved, 0.0);
    }

    @Transactional
    public RestaurantResponse lockRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + id));

        restaurant.setStatus(RestaurantStatus.LOCKED);
        Restaurant saved = restaurantRepository.save(restaurant);
        sendEmailEvent(saved.getOwner().getEmail(), "Suspended",
                "Your restaurant " + saved.getName() + " has been suspended.");

        return restaurantMapper.toResponse(saved, 0.0);
    }

    @Transactional
    public RestaurantResponse reinstateRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found!"));

        if (restaurant.getStatus() != RestaurantStatus.LOCKED) {
            throw new RuntimeException("Only LOCKED restaurants can be reinstated!");
        }

        restaurant.setStatus(RestaurantStatus.ACTIVE);
        Restaurant saved = restaurantRepository.save(restaurant);

        try {
            sendEmailEvent(saved.getOwner().getEmail(), "Restored", "Your restaurant has resumed operations.");

            Map<String, String> notifMsg = Map.of(
                    "message", "Your restaurant '" + saved.getName() + "' has been restored.",
                    "type", "RESTAURANT_REINSTATED"
            );
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_APP, "restaurant_owner_routing_key", objectMapper.writeValueAsString(notifMsg));
        } catch (Exception e) { e.printStackTrace(); }

        return restaurantMapper.toResponse(saved, 0.0);
    }

    @Transactional(readOnly = true)
    public RestaurantAnalyticsResponse getAnalytics(String username, String timeFilter) {
        // Tìm user đang đăng nhập dựa vào username
        User user = userRepository.findByUsername(username).orElseThrow();
        // Tìm nhà hàng mà user này đang làm chủ
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId()).orElseThrow();
        Long resId = restaurant.getId();

        LocalDateTime now = LocalDateTime.now();
        // Nếu user chọn "Last 30 Days" thì mốc bắt đầu trừ đi 30 ngày, mặc định thì trừ 7 ngày
        LocalDateTime currentStart = "Last 30 Days".equals(timeFilter) ? now.minusDays(30) : now.minusDays(7);

        // Chuyển đổi sang kiểu Instant để tương thích với các câu truy vấn chuẩn của JPA/Hibernate
        Instant nowInst = now.atZone(ZoneId.systemDefault()).toInstant();
        Instant startInst = currentStart.atZone(ZoneId.systemDefault()).toInstant();

        // Đếm tổng số lượng đơn hàng trong khoảng thời gian đã chọn
        long totalOrders = orderRepository.countOrdersInPeriod(resId, startInst, nowInst);
        // Tính tổng doanh thu (cộng dồn Total Amount)
        BigDecimal totalRevenue = orderRepository.calculateRevenueInPeriod(resId, startInst, nowInst);
        // Nếu không có đơn nào (kết quả truy vấn là null) thì gán bằng 0 để tránh lỗi NullPointerException
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO; 
        // Tính giá trị trung bình mỗi đơn (AOV) = Tổng doanh thu / Tổng số đơn
        // Kiểm tra totalOrders > 0 để tránh lỗi chia cho 0 (Divide by zero) nếu nhà hàng chưa bán được gì
        BigDecimal avgOrderValue = totalOrders > 0 ? totalRevenue.divide(BigDecimal.valueOf(totalOrders), 0, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        // Chỉ lấy các đơn hàng có trạng thái Đã Hoàn Thành (COMPLETED)
        List<Order> completedOrders = orderRepository.findByRestaurantIdAndStatus(resId, OrderStatus.COMPLETED);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MMM dd").withZone(ZoneId.systemDefault());

        // Nhóm các đơn hàng theo từng ngày
        List<DailyRevenueData> revenueOrderData = completedOrders.stream()
                .collect(Collectors.groupingBy(o -> df.format(o.getCreatedAt())))
                .entrySet().stream()
                .map(e -> new DailyRevenueData(e.getKey(),
                        e.getValue().stream().map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
                        (long) e.getValue().size()))
                .collect(Collectors.toList());

       
        List<Tuple> rawPeakHours = orderRepository.findPeakHourStats(resId);
        List<RestaurantAnalyticsResponse.PeakHourData> peakHoursData = rawPeakHours.stream()
                .map(tuple -> new RestaurantAnalyticsResponse.PeakHourData(
                        tuple.get(0, String.class),                
                        ((Number) tuple.get(1)).longValue()         
                ))
                .collect(Collectors.toList());

        
        List<Tuple> rawCategorySales = orderRepository.findSalesByCategory(resId);
        List<RestaurantAnalyticsResponse.CategorySalesData> salesByCategory = rawCategorySales.stream()
                .map(tuple -> new RestaurantAnalyticsResponse.CategorySalesData(
                        tuple.get(0, String.class),               
                        ((Number) tuple.get(1)).longValue(),        
                        tuple.get(2) != null ? new BigDecimal(tuple.get(2).toString()) : BigDecimal.ZERO, 
                        "#3b82f6"
                ))
                .collect(Collectors.toList());


        AtomicInteger rankCounter = new AtomicInteger(1);
        List<Object[]> rawTopItems = orderRepository.findTopSellingItems(resId);
        List<RestaurantAnalyticsResponse.TopMenuItemData> topMenuItems = rawTopItems.stream()
        .map(row -> new RestaurantAnalyticsResponse.TopMenuItemData(
                rankCounter.getAndIncrement(),               
                (String) row[0],                               
                "N/A",                                        
                row[3] != null ? (String) row[3] : "N/A",      
                row[5] != null ? String.valueOf(row[5]) : "0", 
                ((Number) row[1]).intValue(),                  
                row[2] != null ? String.valueOf(row[2]) : "0", 
                "0%",                                        
                "vs last week",                              
                new java.util.ArrayList<>()                    
        ))
        .collect(Collectors.toList());
      
        return new RestaurantAnalyticsResponse(
                totalRevenue, 0.0, totalOrders, 0.0, avgOrderValue, 0.0, 0.0, 0.0, 
                revenueOrderData, 
                peakHoursData,  
                salesByCategory, 
                topMenuItems     
        );
    }


    private double calculatePercentageChange(BigDecimal previous, BigDecimal current) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) return 0.0;
        return current.subtract(previous).divide(previous, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
    }

    @Transactional(readOnly = true)
    public byte[] exportRestaurantReport(String username) throws IOException {
        User user = userRepository.findByUsername(username).orElseThrow();
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId()).orElseThrow();

        List<OrderStatus> reportStatuses = List.of(OrderStatus.DELIVERED, OrderStatus.COMPLETED);
        List<Order> orders = orderRepository.findByRestaurantIdAndStatusIn(restaurant.getId(), reportStatuses);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Analytics Report");
            
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Order ID");
            header.createCell(1).setCellValue("Customer");
            header.createCell(2).setCellValue("Amount");
            header.createCell(3).setCellValue("Status");

            int rowIdx = 1;
            for (Order order : orders) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getCustomer().getFullname()); 
                row.createCell(2).setCellValue(order.getTotalAmount().doubleValue());
                row.createCell(3).setCellValue(order.getStatus().toString());
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            workbook.write(out);
            return out.toByteArray();
        }
    }
}