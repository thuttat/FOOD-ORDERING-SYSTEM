package duckie.example.backend.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duckie.example.backend.config.RabbitMQConfig;
import duckie.example.backend.entity.Order;
import duckie.example.backend.entity.OrderStatus;
import duckie.example.backend.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(OrderRepository orderRepository, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng có ID: " + orderId));

        order.setStatus(newStatus);
        Order savedOrder = orderRepository.save(order);
        String message = String.format("Đơn hàng #%d đã chuyển sang trạng thái: %s", savedOrder.getId(), newStatus.name());
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_ORDER, 
            RabbitMQConfig.ROUTING_KEY_ORDER, 
            message
        );
        System.out.println(" Đã gửi thông báo vào RabbitMQ: " + message);
        return savedOrder;
    }
}