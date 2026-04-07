package duckie.example.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;

import duckie.example.backend.dto.RestaurantRequest;
import duckie.example.backend.dto.RestaurantResponse;

public interface RestaurantService {
    List<RestaurantResponse> findAllActive();
    Page<RestaurantResponse> getAllRestaurants(int page, int size);
    Page<RestaurantResponse> getPendingRestaurants(int page, int size);
    RestaurantResponse createRestaurant(RestaurantRequest request, boolean isAdminCreated);
    RestaurantResponse approveRestaurant(Long id);
    RestaurantResponse lockRestaurant(Long id);
}