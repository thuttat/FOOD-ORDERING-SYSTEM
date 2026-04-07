package duckie.example.backend.service;

import duckie.example.backend.dto.RestaurantRequest;
import duckie.example.backend.dto.RestaurantResponse;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.RestaurantStatus;
import duckie.example.backend.entity.Role;
import duckie.example.backend.entity.User;
import duckie.example.backend.exception.ResourceNotFoundException;
import duckie.example.backend.repository.RestaurantRepository;
import duckie.example.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<RestaurantResponse> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return restaurantRepository.findAll(pageable).map(this::mapToRecord);
    }

    @Override
    public Page<RestaurantResponse> getPendingRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Restaurant> pendingRestaurants = restaurantRepository.findByStatus(RestaurantStatus.PENDING, pageable);
        return pendingRestaurants.map(this::mapToRecord);
    }

    @Transactional
    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest request, boolean isAdminCreated) {
        User user = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (user.getRole() != Role.RESTAURANT) {
            throw new RuntimeException("You have not permission to be restaurant owner!");
        }

        Restaurant restaurant = Restaurant.builder()
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .address(request.address())
                .description(request.description())
                .imageUrl(request.imageUrl())
                .owner(user)
                .status(isAdminCreated ? RestaurantStatus.ACTIVE : RestaurantStatus.PENDING)
                .build();

        Restaurant saved = restaurantRepository.save(restaurant);
        return mapToRecord(saved);
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
        return mapToRecord(saved);
    }

    @Transactional
    @Override
    public RestaurantResponse lockRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found!"));

        restaurant.setStatus(RestaurantStatus.LOCKED);
        Restaurant saved = restaurantRepository.save(restaurant);
        return mapToRecord(saved);
    }

    private RestaurantResponse mapToRecord(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getOwner().getFullname(),
                restaurant.getAddress(),
                restaurant.getDescription(),
                restaurant.getImageUrl(),
                restaurant.getStatus(),
                restaurant.getCreatedAt()
        );
    }
}
