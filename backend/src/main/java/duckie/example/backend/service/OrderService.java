package duckie.example.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import duckie.example.backend.dto.OrderRequest;
import duckie.example.backend.dto.OrderResponse;
import duckie.example.backend.entity.*;
import duckie.example.backend.repository.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final RestaurantRepository restaurantRepository;

    private final MenuItemRepository menuItemRepository;

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;


    public OrderService(OrderRepository orderRepository, 
                        RestaurantRepository restaurantRepository,
                        MenuItemRepository menuItemRepository,
                        OrderMapper orderMapper,
                        OrderItemMapper orderItemMapper) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
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
        
        return orderMapper.toResponse(order);
    }
}