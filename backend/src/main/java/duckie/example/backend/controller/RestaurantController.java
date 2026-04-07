package duckie.example.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import duckie.example.backend.service.RestaurantService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantResponse>> findAllActiveRestaurants() {
        List<RestaurantResponse> restaurants = restaurantService.findAllActive();
        return ResponseEntity.ok(restaurants);
    }
    @GetMapping("/admin/restaurants")
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RestaurantResponse> restaurantResponse = restaurantService.getAllRestaurants(page, size);
        return ResponseEntity.ok(restaurantResponse.getContent());
    }

    @PostMapping("/admin/restaurants")
    public ResponseEntity<RestaurantResponse> createRestaurantByAdmin(@Valid @RequestBody RestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.createRestaurant(request, true));
    }

    @GetMapping("/admin/restaurants/pending")
    public ResponseEntity<List<RestaurantResponse>> getPendingRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RestaurantResponse> restaurantResponse = restaurantService.getPendingRestaurants(page, size);
        return ResponseEntity.ok(restaurantResponse.getContent());
    }

    @PatchMapping("/admin/restaurants/{id}/approve")
    public ResponseEntity<RestaurantResponse> approveRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.approveRestaurant(id));
    }

    @PatchMapping("/admin/restaurants/{id}/lock")
    public ResponseEntity<RestaurantResponse> lockRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.lockRestaurant(id));
    }
}