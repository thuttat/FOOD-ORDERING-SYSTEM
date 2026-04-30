package duckie.example.backend.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duckie.example.backend.config.RabbitMQConfig;
import duckie.example.backend.dto.RestaurantRequest;
import duckie.example.backend.dto.RestaurantResponse;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.RestaurantStatus;
import duckie.example.backend.entity.User;
import duckie.example.backend.exception.ResourceNotFoundException;
import duckie.example.backend.mapper.RestaurantMapper;
import duckie.example.backend.repository.RestaurantRepository;
import duckie.example.backend.repository.UserRepository;
import tools.jackson.databind.ObjectMapper;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RestaurantMapper restaurantMapper;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 UserRepository userRepository,
                                 RestaurantMapper restaurantMapper,
                                 RabbitTemplate rabbitTemplate,
                                 ObjectMapper objectMapper) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.restaurantMapper = restaurantMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<RestaurantResponse> findAllActive() {
        return restaurantRepository.findByIsOpenTrueAndStatus(RestaurantStatus.ACTIVE).stream()
                .map(r -> restaurantMapper.toResponse(r, 5.0))
                .collect(Collectors.toList());
    }

    @Override
    public Page<RestaurantResponse> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return restaurantRepository.findAll(pageable)
                .map(r -> restaurantMapper.toResponse(r, 0.0));
    }

    @Override
    public RestaurantResponse getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + id));
        return restaurantMapper.toResponse(restaurant, 0.0);
    }

    @Transactional
    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest request, boolean isAdminCreated) {
        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + request.ownerId()));

        Restaurant restaurant = restaurantMapper.toEntity(request, owner);
        restaurant.setStatus(isAdminCreated ? RestaurantStatus.ACTIVE : RestaurantStatus.PENDING);

        Restaurant saved = restaurantRepository.save(restaurant);

        if (!isAdminCreated) {
            try {
                java.util.Map<String, String> msg = java.util.Map.of(
                        "message", "New restaurant registration: " + saved.getName() + " is awaiting approval!",
                        "type", "RESTAURANT_PENDING"
                );
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_APP, RabbitMQConfig.ROUTING_KEY_ADMIN, objectMapper.writeValueAsString(msg));
            } catch (Exception e) { e.printStackTrace(); }
        }
        return restaurantMapper.toResponse(saved, 0.0);
    }

    @Transactional
    @Override
    public RestaurantResponse approveRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.setStatus(RestaurantStatus.ACTIVE);

        Restaurant saved = restaurantRepository.save(restaurant);
        sendEmailEvent(saved.getOwner().getEmail(), "Congratulations! Your restaurant has been approved.",
                "Your restaurant " + saved.getName() + " has been approved by the Admin and can now begin operations.");
        return restaurantMapper.toResponse(saved, 0.0);
    }

    @Transactional
    @Override
    public RestaurantResponse lockRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.setStatus(RestaurantStatus.LOCKED);

        Restaurant saved = restaurantRepository.save(restaurant);
        sendEmailEvent(saved.getOwner().getEmail(), "Announcement: The restaurant is suspended.",
                "Your restaurant " + saved.getName() + " has been suspended by the Admin. Please contact us for support.");
        return restaurantMapper.toResponse(saved, 0.0);
    }

    private void sendEmailEvent(String to, String subject, String body) {
        try {
            Map<String, String> emailData = Map.of("to", to, "subject", subject, "body", body);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_APP, RabbitMQConfig.ROUTING_KEY_EMAIL, objectMapper.writeValueAsString(emailData));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public Page<RestaurantResponse> getPendingRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return restaurantRepository.findByStatus(RestaurantStatus.PENDING, pageable)
                .map(r -> restaurantMapper.toResponse(r, 0.0));
    }

    @Transactional
    @Override
    public RestaurantResponse reinstateRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found!"));

        if (restaurant.getStatus() != RestaurantStatus.LOCKED) {
            throw new RuntimeException("Only LOCKED restaurants can be reinstated!");
        }

        restaurant.setStatus(RestaurantStatus.ACTIVE);
        Restaurant saved = restaurantRepository.save(restaurant);
        try {
            sendEmailEvent(
                    saved.getOwner().getEmail(),
                    "Announcement: Your restaurant has resumed operations.",
                    "Hello " + saved.getOwner().getFullname() + ",\n\n" +
                            "We are pleased to announce that your restaurant '" + saved.getName() + "' has been restored to operation on the HappyFood system by the Admin.\n" +
                            "You can now log in and continue managing your business operations.\n\n" +
                            "Sincerely,\nHappyFood Admin Team."
            );

            Map<String, String> notifMsg = Map.of(
                    "message", "Your restaurant '" + saved.getName() + "' has been restored. Wishing you great success in your business!",
                    "type", "RESTAURANT_REINSTATED"
            );
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_APP, "restaurant_owner_routing_key", objectMapper.writeValueAsString(notifMsg));

        } catch (Exception e) {
            System.err.println("Error sending reinstated notification: " + e.getMessage());
        }
        return restaurantMapper.toResponse(saved, 0.0);
    }
}