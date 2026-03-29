package duckie.example.backend.service;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.UserResponse;
import duckie.example.backend.entity.User;

@Component
public class UserMapper {
    public UserResponse toResponse(User user){
        if(user == null) return null;
        return new UserResponse(
            user.getId(),
            user.getFullname(),
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.getStatus(),
            user.getCreatedAt(),
            user.getUpdatedAt() 
        );
    }
}