package duckie.example.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.RestaurantStatus;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByIsOpenTrueAndStatus(RestaurantStatus status);
    List<Restaurant> findByOwnerId(Long ownerId); // Bổ sung cho chủ quán quản lý
    Page<Restaurant> findByStatus(RestaurantStatus status, Pageable pageable);
}