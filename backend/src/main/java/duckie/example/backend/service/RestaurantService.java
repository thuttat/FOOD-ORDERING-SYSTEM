package duckie.example.backend.service;

import duckie.example.backend.dto.RestaurantRequest;
import duckie.example.backend.dto.RestaurantResponse;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface RestaurantService {
    Page<RestaurantResponse> getAllRestaurants(int page, int size);

    Page<RestaurantResponse> getPendingRestaurants(int page, int size);

    @Transactional
    RestaurantResponse createRestaurant(RestaurantRequest request, boolean isAdminCreated);

    @Transactional
    RestaurantResponse approveRestaurant(Long id);

    @Transactional
    RestaurantResponse lockRestaurant(Long id);
}
