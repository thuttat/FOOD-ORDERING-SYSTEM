package duckie.example.backend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_ORDER_NOTIFICATION = "order_notification_queue";
    public static final String EXCHANGE_ORDER = "order_exchange";
    public static final String ROUTING_KEY_ORDER = "order_routing_key";
    public static final String QUEUE_ADMIN_NOTIFICATION = "admin_notification_queue";
    public static final String QUEUE_EMAIL = "email_queue";
    public static final String EXCHANGE_APP = "app_exchange";
    public static final String ROUTING_KEY_ADMIN = "admin_routing_key";
    public static final String ROUTING_KEY_EMAIL = "email_routing_key";

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

    @Bean public DirectExchange appExchange() { return new DirectExchange(EXCHANGE_APP); }

    @Bean public Queue adminNotificationQueue() { return new Queue(QUEUE_ADMIN_NOTIFICATION, true); }
    @Bean public Queue emailQueue() { return new Queue(QUEUE_EMAIL, true); }

    @Bean public Binding bindingAdminNotification() {
        return BindingBuilder.bind(adminNotificationQueue()).to(appExchange()).with(ROUTING_KEY_ADMIN);
    }
    @Bean public Binding bindingEmail() {
        return BindingBuilder.bind(emailQueue()).to(appExchange()).with(ROUTING_KEY_EMAIL);
    }
}