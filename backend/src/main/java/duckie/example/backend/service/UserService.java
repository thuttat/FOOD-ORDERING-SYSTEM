package duckie.example.backend.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import duckie.example.backend.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duckie.example.backend.dto.UserPatchRequest;
import duckie.example.backend.dto.UserResponse;
import duckie.example.backend.entity.User;
import duckie.example.backend.entity.UserStatus;
import duckie.example.backend.mapper.UserMapper;
import duckie.example.backend.repository.UserRepository;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(String search, Role role, UserStatus status, Pageable pageable) {
        return userRepository.findAllBySearchAndRoleAndStatus(search, role, status, pageable)
                .map(userMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse patchUpdate(Long id, UserPatchRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        userMapper.partialUpdate(user, request);
        
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        User savedUser = userRepository.save(user);
        logger.info("Updated user profile for ID: {}", id);
        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponse updateStatus(Long id, UserStatus newStatus) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setStatus(newStatus);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateRole(Long id, Role newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setRole(newRole);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getUserStats() {
        long total = userRepository.count();
        long activeCustomers = userRepository.countByRoleAndStatus(Role.USER, UserStatus.ACTIVE);
        long activeRestaurants = userRepository.countByRoleAndStatus(Role.RESTAURANT, UserStatus.ACTIVE);

        List<Object[]> rawData = userRepository.getRawUserRegistrationData();
        List<Map<String, Object>> growthData = new java.util.ArrayList<>();
        String[] monthNames = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        for (int i = 1; i <= 12; i++) {
            Map<String, Object> mData = new java.util.HashMap<>();
            mData.put("month", monthNames[i]);
            mData.put("users", 0L);
            growthData.add(mData);
        }

        for (Object[] row : rawData) {
            int monthIndex = Integer.parseInt(row[0].toString());
            growthData.get(monthIndex - 1).put("users", Long.parseLong(row[1].toString()));
        }

        return Map.of(
                "total", total,
                "activeCustomers", activeCustomers,
                "activeRestaurants", activeRestaurants,
                "growthData", growthData
        );
    }
}