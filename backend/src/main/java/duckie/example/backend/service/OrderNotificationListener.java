package duckie.example.backend.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import duckie.example.backend.config.RabbitMQConfig;

@Component
public class OrderNotificationListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_NOTIFICATION)
    public void receiveMessage(String message) {
        System.out.println("NHÀ HÀNG NHẬN ĐƯỢC THÔNG BÁO:  " + message);
    }
}