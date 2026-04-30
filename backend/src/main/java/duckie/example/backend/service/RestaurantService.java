package duckie.example.backend.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException; // Chỉ dùng IOException chuẩn của Java
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        User user = userRepository.findByUsername(username).orElseThrow();
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId()).orElseThrow();
        Long resId = restaurant.getId();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentStart = "Last 30 Days".equals(timeFilter) ? now.minusDays(30) : now.minusDays(7);

        java.time.Instant nowInst = now.atZone(ZoneId.systemDefault()).toInstant();
        java.time.Instant startInst = currentStart.atZone(ZoneId.systemDefault()).toInstant();

        long totalOrders = orderRepository.countOrdersInPeriod(resId, startInst, nowInst);
        BigDecimal totalRevenue = orderRepository.calculateRevenueInPeriod(resId, startInst, nowInst);
        BigDecimal avgOrderValue = totalOrders > 0 ? totalRevenue.divide(BigDecimal.valueOf(totalOrders), 0, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        List<Order> completedOrders = orderRepository.findByRestaurantIdAndStatus(resId, OrderStatus.DELIVERED);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MMM dd").withZone(ZoneId.systemDefault());

        List<DailyRevenueData> revenueOrderData = completedOrders.stream()
                .collect(Collectors.groupingBy(o -> df.format(o.getCreatedAt())))
                .entrySet().stream()
                .map(e -> new DailyRevenueData(e.getKey(),
                        e.getValue().stream().map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
                        (long) e.getValue().size()))
                .collect(Collectors.toList());

        return new RestaurantAnalyticsResponse(totalRevenue, 0.0, totalOrders, 0.0, avgOrderValue, 0.0, 0.0, 0.0, revenueOrderData, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    private double calculatePercentageChange(BigDecimal previous, BigDecimal current) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) return 0.0;
        return current.subtract(previous).divide(previous, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
    }

    @Transactional(readOnly = true)
    public byte[] exportRestaurantReport(String username) throws IOException {
        User user = userRepository.findByUsername(username).orElseThrow();
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId()).orElseThrow();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Analytics Report");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Order ID");
            header.createCell(1).setCellValue("Customer");
            header.createCell(2).setCellValue("Amount");

            workbook.write(out);
            return out.toByteArray();
        }
    }
}