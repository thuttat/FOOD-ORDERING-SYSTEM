package duckie.example.backend.repository;

import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.entity.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findByStatus(RestaurantStatus status, Pageable pageable);
}
