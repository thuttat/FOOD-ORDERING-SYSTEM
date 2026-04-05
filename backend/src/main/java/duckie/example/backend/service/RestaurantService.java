package duckie.example.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import duckie.example.backend.dto.RestaurantRequest;
import duckie.example.backend.dto.RestaurantResponse;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.User;
import duckie.example.backend.repository.RestaurantRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;

    private final RestaurantMapper restaurantMapper;


    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }


    @Transactional(readOnly = true)
    public List<RestaurantResponse> findAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public RestaurantResponse create(RestaurantRequest request, User owner) {
        Restaurant restaurant = restaurantMapper.toEntity(request, owner);

        restaurant = restaurantRepository.save(restaurant);
        logger.info("Created restaurant with id: {}", restaurant.getId());
        
        return restaurantMapper.toResponse(restaurant);
    }
}