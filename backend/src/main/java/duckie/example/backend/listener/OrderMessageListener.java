package duckie.example.backend.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import duckie.example.backend.config.RabbitMQConfig;
import duckie.example.backend.dto.OrderResponse;

@Component
public class OrderMessageListener {

    private final SimpMessagingTemplate messagingTemplate;

    public OrderMessageListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_NOTIFICATION)
    public void receiveOrderMessage(OrderResponse orderResponse) {
        messagingTemplate.convertAndSend("/topic/orders", orderResponse);
    }
}