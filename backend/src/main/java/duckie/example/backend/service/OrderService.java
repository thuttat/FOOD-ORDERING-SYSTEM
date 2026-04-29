package duckie.example.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import duckie.example.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duckie.example.backend.config.RabbitMQConfig;
import duckie.example.backend.dto.OrderRequest;
import duckie.example.backend.dto.OrderResponse;
import duckie.example.backend.entity.*;
import duckie.example.backend.mapper.OrderItemMapper;
import duckie.example.backend.mapper.OrderMapper;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        OrderMapper orderMapper, OrderItemMapper orderItemMapper,
                        RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public OrderResponse createOrder(String username, OrderRequest request) {
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Can not find this user"));

        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new RuntimeException("Cart does not exist"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) throw new RuntimeException("Cart is blank");

        Restaurant restaurant = cartItems.get(0).getMenuItem().getCategory().getRestaurant();
        Order order = orderMapper.toEntity(request, customer, restaurant);

        List<OrderItem> orderItems = cartItems.stream()
                .map(ci -> orderItemMapper.fromCartItem(ci, order))
                .collect(Collectors.toList());
        order.setItems(orderItems);

        BigDecimal total = orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(order.getDeliveryFee());
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);


        sendRabbitNotification("Bill created: #" + savedOrder.getId());

        return orderMapper.toResponse(savedOrder);
    }

    private void sendRabbitNotification(String message) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ORDER, RabbitMQConfig.ROUTING_KEY_ORDER, message);
            logger.info("RabbitMQ Notification Success: {}", message);
        } catch (Exception e) {
            logger.error("RabbitMQ Notification Failed: {}", e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrderHistory(String username) {
        User customerUser = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerUser.getId()).stream().map(orderMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderDetail(Long orderId, String username) {
        User customerUser = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if (!order.getCustomer().getId().equals(customerUser.getId())) throw new RuntimeException("Not authorized");
        return orderMapper.toResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newStatus);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }
}