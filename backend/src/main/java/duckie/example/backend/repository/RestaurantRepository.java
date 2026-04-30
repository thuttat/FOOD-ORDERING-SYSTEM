package duckie.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.RestaurantStatus;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByStatus(RestaurantStatus status);

    Optional<Restaurant> findByOwnerId(Long ownerId);

    List<Restaurant> findByIsOpenTrueAndStatus(RestaurantStatus status);

    List<Restaurant> findAllByOwnerId(Long ownerId);

    Page<Restaurant> findByStatus(RestaurantStatus status, Pageable pageable);
}