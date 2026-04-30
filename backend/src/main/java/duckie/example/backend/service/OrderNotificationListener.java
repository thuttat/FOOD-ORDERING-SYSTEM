package duckie.example.backend.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import duckie.example.backend.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderNotificationListener {
    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_NOTIFICATION)
    public void handleOrderNotification(Object orderData) {
        System.out.println("Nhận đơn hàng mới từ RabbitMQ: " + orderData);
        messagingTemplate.convertAndSend("/topic/orders", orderData);
    }
}