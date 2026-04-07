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
import duckie.example.backend.entity.Role;
import duckie.example.backend.entity.User;
import duckie.example.backend.exception.ResourceNotFoundException;
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
    @Transactional(readOnly = true)
    public List<RestaurantResponse> findAllActive() {
        return restaurantRepository.findByIsOpenTrue().stream()
                .map(restaurantMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantResponse> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return restaurantRepository.findAll(pageable).map(restaurantMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantResponse> getPendingRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Restaurant> pendingRestaurants = restaurantRepository.findByStatus(RestaurantStatus.PENDING, pageable);
        return pendingRestaurants.map(restaurantMapper::toResponse);
    }

    @Transactional
    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest request, boolean isAdminCreated) {
        User user = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (user.getRole() != Role.RESTAURANT) {
            throw new RuntimeException("You do not have permission to be a restaurant owner!");
        }

        Restaurant restaurant = restaurantMapper.toEntity(request, user);
        restaurant.setStatus(isAdminCreated ? RestaurantStatus.ACTIVE : RestaurantStatus.PENDING);

        Restaurant saved = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public RestaurantResponse approveRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found!"));

        if (restaurant.getStatus() != RestaurantStatus.PENDING) {
            throw new RuntimeException("Only PENDING restaurants can be approved!");
        }

        restaurant.setStatus(RestaurantStatus.ACTIVE);
        Restaurant saved = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public RestaurantResponse lockRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found!"));

        restaurant.setStatus(RestaurantStatus.LOCKED);
        Restaurant saved = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(saved);
    }
}