package duckie.example.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import duckie.example.backend.dto.RestaurantRequest;
import duckie.example.backend.dto.RestaurantResponse;
import duckie.example.backend.service.RestaurantServiceImpl;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class RestaurantController {

    private final RestaurantServiceImpl restaurantServiceImpl;

    public RestaurantController(RestaurantServiceImpl restaurantServiceImpl) {
        this.restaurantServiceImpl = restaurantServiceImpl;
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantResponse>> findAllActiveRestaurants() {
        List<RestaurantResponse> restaurants = restaurantServiceImpl.findAllActive();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long id) {
        RestaurantResponse response = restaurantServiceImpl.getRestaurantById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/restaurants")
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RestaurantResponse> restaurantResponse = restaurantServiceImpl.getAllRestaurants(page, size);
        return ResponseEntity.ok(restaurantResponse.getContent());
    }

    @PostMapping("/admin/restaurants")
    public ResponseEntity<RestaurantResponse> createRestaurantByAdmin(@Valid @RequestBody RestaurantRequest request) {
        return ResponseEntity.ok(restaurantServiceImpl.createRestaurant(request, true));
    }

    @GetMapping("/admin/restaurants/pending")
    public ResponseEntity<List<RestaurantResponse>> getPendingRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RestaurantResponse> restaurantResponse = restaurantServiceImpl.getPendingRestaurants(page, size);
        return ResponseEntity.ok(restaurantResponse.getContent());
    }

    @PatchMapping("/admin/restaurants/{id}/approve")
    public ResponseEntity<RestaurantResponse> approveRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantServiceImpl.approveRestaurant(id));
    }

    @PatchMapping("/admin/restaurants/{id}/lock")
    public ResponseEntity<RestaurantResponse> lockRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantServiceImpl.lockRestaurant(id));
    }

    @PatchMapping("/admin/restaurants/{id}/reinstate")
    public ResponseEntity<RestaurantResponse> reinstateRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantServiceImpl.reinstateRestaurant(id));
    }
}