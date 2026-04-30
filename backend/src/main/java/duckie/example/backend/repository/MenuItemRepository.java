package duckie.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import duckie.example.backend.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    @Query("SELECT m FROM MenuItem m WHERE m.category.restaurant.owner.username = :username")
    List<MenuItem> findByOwnerUsername(@Param("username") String username);
    
    List<MenuItem> findByCategoryName(String categoryName);
    List<MenuItem> findByCategoryIdAndIsAvailableTrue(Long categoryId);

    List<MenuItem> findByNameContainingIgnoreCase(String name);
}