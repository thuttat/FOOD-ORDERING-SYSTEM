package duckie.example.backend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_ORDER_NOTIFICATION = "order_notification_queue";
    public static final String EXCHANGE_ORDER = "order_exchange";
    public static final String ROUTING_KEY_ORDER = "order_routing_key";

    @Bean
    public Queue orderNotificationQueue() {
        return new Queue(QUEUE_ORDER_NOTIFICATION, true); 
    }
   
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(EXCHANGE_ORDER);
    }

    @Bean
    public Binding bindingOrderNotification(Queue orderNotificationQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderNotificationQueue).to(orderExchange).with(ROUTING_KEY_ORDER);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}