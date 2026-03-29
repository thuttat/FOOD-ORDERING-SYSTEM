package duckie.example.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import duckie.example.backend.entity.Role;
import duckie.example.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email,Long id);
    boolean existsByUsernameAndIdNot(String username,Long id);

    @Query("SELECT u FROM User u WHERE " +
           "(:search IS NULL OR u.username LIKE %:search% OR u.email LIKE %:search%) AND " +
           "(:role IS NULL OR u.role = :role)")
    Page<User> findAllBySearchAndRole(@Param("search") String search, @Param("role") Role role, Pageable pageable);
}
