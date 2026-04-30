package duckie.example.backend.repository;

import java.util.List;
import java.util.Optional;

import duckie.example.backend.entity.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import duckie.example.backend.entity.Role;
import duckie.example.backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByUsernameAndIdNot(String username, Long id);

    @Query("SELECT u FROM User u WHERE " +
           "(:search IS NULL OR u.username LIKE %:search% OR u.email LIKE %:search% OR u.fullname LIKE %:search%) AND " +
           "(:role IS NULL OR u.role = :role)")
    Page<User> findAllBySearchAndRole(@Param("search") String search, @Param("role") Role role, Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
            "(:search IS NULL OR u.username LIKE %:search% OR u.email LIKE %:search% OR u.fullname LIKE %:search%) AND " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:status IS NULL OR u.status = :status)")
    Page<User> findAllBySearchAndRoleAndStatus(
            @Param("search") String search,
            @Param("role") Role role,
            @Param("status") UserStatus status,
            Pageable pageable
    );

    long countByRoleAndStatus(Role role, UserStatus status);

    @Query("SELECT FUNCTION('MONTH', u.createdAt), COUNT(u) " +
            "FROM User u " +
            "WHERE FUNCTION('YEAR', u.createdAt) = FUNCTION('YEAR', CURRENT_DATE) " +
            "GROUP BY FUNCTION('MONTH', u.createdAt) " +
            "ORDER BY FUNCTION('MONTH', u.createdAt) ASC")
    List<Object[]> getRawUserRegistrationData();
}