package duckie.example.backend.controller;

import duckie.example.backend.dto.RestaurantRequest;
import duckie.example.backend.dto.RestaurantResponse;
import duckie.example.backend.service.RestaurantService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RestaurantResponse> restaurantResponse = restaurantService.getAllRestaurants(page, size);
        return ResponseEntity.ok(restaurantResponse.getContent());
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(@RequestBody RestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.createRestaurant(request, true));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<RestaurantResponse>> getPendingRestaurants (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RestaurantResponse> restaurantResponse = restaurantService.getPendingRestaurants(page, size);
        return ResponseEntity.ok(restaurantResponse.getContent());
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<RestaurantResponse> approveRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.approveRestaurant(id));
    }

    @PatchMapping("/{id}/lock")
    public ResponseEntity<RestaurantResponse> lockRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.lockRestaurant(id));
    }
}
