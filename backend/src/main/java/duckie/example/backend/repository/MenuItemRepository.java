package duckie.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import duckie.example.backend.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    List<MenuItem> findByCategoryIdAndIsAvailableTrue(Long categoryId);
    
    List<MenuItem> findByNameContainingIgnoreCase(String name);
}