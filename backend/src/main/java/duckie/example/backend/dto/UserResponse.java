package duckie.example.backend.dto;

import java.time.Instant;

import duckie.example.backend.entity.Role;
import duckie.example.backend.entity.UserStatus;

public record UserResponse( 
    Long id,
    String fullname,
    String username,
    String email,
    String phone, 
    Role role,
    UserStatus status,
    Instant createdAt,
    Instant updatedAt
){}