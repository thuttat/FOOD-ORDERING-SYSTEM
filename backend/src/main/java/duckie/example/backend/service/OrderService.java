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
import duckie.example.backend.dto.PaymentRequest;
import duckie.example.backend.entity.Cart;
import duckie.example.backend.entity.CartItem;
import duckie.example.backend.entity.Order;
import duckie.example.backend.entity.OrderItem;
import duckie.example.backend.entity.OrderStatus;
import duckie.example.backend.entity.Payment;
import duckie.example.backend.entity.PaymentMethod;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.User;
import duckie.example.backend.mapper.OrderItemMapper;
import duckie.example.backend.mapper.OrderMapper;
import duckie.example.backend.mapper.PaymentMapper;
import duckie.example.backend.repository.CartItemRepository;
import duckie.example.backend.repository.CartRepository;
import duckie.example.backend.repository.OrderRepository;
import duckie.example.backend.repository.PaymentRepository;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final PaymentMapper paymentMapper;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository,
                        CartItemRepository cartItemRepository, PaymentRepository paymentRepository,
                        OrderMapper orderMapper, OrderItemMapper orderItemMapper,
                        PaymentMapper paymentMapper, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.paymentRepository = paymentRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.paymentMapper = paymentMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public OrderResponse createOrder(User customer, OrderRequest request) {
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

        PaymentMethod method = request.paymentMethod(); 
        
        Payment payment = paymentMapper.toEntity(
            new PaymentRequest(savedOrder.getId(), method, savedOrder.getTotalAmount()), 
            savedOrder, 
            "TXN_" + System.currentTimeMillis()
        );
        paymentRepository.save(payment);
        cartItemRepository.deleteByCartId(cart.getId());
        
        sendNotification("Bill created: #" + savedOrder.getId());
        
        return orderMapper.toResponse(savedOrder);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("cannot find bill ID: " + orderId));

        order.setStatus(newStatus);
        Order savedOrder = orderRepository.save(order);
        
        String message = String.format("Bill #%d changed status into : %s", savedOrder.getId(), newStatus.name());
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
            logger.info("RabbitMQ Notification Success: {}", message);
        } catch (Exception e) {
            logger.error("RabbitMQ Notification Failed: {}", e.getMessage());
        }
    }
}