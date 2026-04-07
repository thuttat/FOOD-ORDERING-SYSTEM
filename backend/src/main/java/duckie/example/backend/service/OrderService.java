package duckie.example.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duckie.example.backend.config.RabbitMQConfig;
import duckie.example.backend.dto.OrderRequest;
import duckie.example.backend.dto.OrderResponse;
import duckie.example.backend.entity.MenuItem;
import duckie.example.backend.entity.Order;
import duckie.example.backend.entity.OrderItem;
import duckie.example.backend.entity.OrderStatus;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.User;
import duckie.example.backend.repository.MenuItemRepository;
import duckie.example.backend.repository.OrderRepository;
import duckie.example.backend.repository.RestaurantRepository;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(OrderRepository orderRepository, 
                        RestaurantRepository restaurantRepository,
                        MenuItemRepository menuItemRepository,
                        OrderMapper orderMapper,
                        OrderItemMapper orderItemMapper,
                        RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse create(OrderRequest request, User customer) {
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà hàng"));

        Order order = orderMapper.toEntity(request, customer, restaurant);

        BigDecimal subTotal = BigDecimal.ZERO;
        
        for (var itemReq : request.items()) {
            MenuItem menuItem = menuItemRepository.findById(itemReq.menuItemId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn"));

            OrderItem orderItem = orderItemMapper.toEntity(itemReq, order, menuItem);
            order.getItems().add(orderItem);
            
            BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity()));
            subTotal = subTotal.add(itemTotal);
        }

        order.setTotalAmount(subTotal.add(order.getDeliveryFee()));
        
        order = orderRepository.save(order);
        logger.info("Created new order with id: {}", order.getId());
        String msg = "New order created: #" + order.getId();
        sendNotification(msg);
        
        return orderMapper.toResponse(order);
    }
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setStatus(newStatus);
        Order savedOrder = orderRepository.save(order);
        
        String message = String.format("Order #%d status updated to: %s", savedOrder.getId(), newStatus.name());
        sendNotification(message);
        
        return orderMapper.toResponse(savedOrder);
    }
    private void sendNotification(String message) {
        try {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_ORDER, 
                RabbitMQConfig.ROUTING_KEY_ORDER, 
                message
            );
            logger.info("Notification sent to RabbitMQ: {}", message);
        } catch (Exception e) {
            logger.error("Failed to send RabbitMQ notification: {}", e.getMessage());
        }
    }
}