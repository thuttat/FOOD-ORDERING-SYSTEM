package duckie.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duckie.example.backend.dto.RestaurantRequest;
import duckie.example.backend.dto.RestaurantResponse;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.RestaurantStatus;
import duckie.example.backend.entity.User;
import duckie.example.backend.exception.ResourceNotFoundException;
import duckie.example.backend.mapper.RestaurantMapper;
import duckie.example.backend.repository.RestaurantRepository;
import duckie.example.backend.repository.UserRepository;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, 
                                 UserRepository userRepository, 
                                 RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.restaurantMapper = restaurantMapper;
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

    @Transactional
    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest request, boolean isAdminCreated) {
        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + request.ownerId()));

        Restaurant restaurant = restaurantMapper.toEntity(request, owner);
        restaurant.setStatus(isAdminCreated ? RestaurantStatus.ACTIVE : RestaurantStatus.PENDING);

        Restaurant saved = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(saved, 0.0);
    }

    @Transactional
    @Override
    public RestaurantResponse approveRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.setStatus(RestaurantStatus.ACTIVE);
        return restaurantMapper.toResponse(restaurantRepository.save(restaurant), 0.0);
    }

    @Transactional
    @Override
    public RestaurantResponse lockRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.setStatus(RestaurantStatus.LOCKED);
        return restaurantMapper.toResponse(restaurantRepository.save(restaurant), 0.0);
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
        return restaurantMapper.toResponse(saved, 0.0);
    }
}