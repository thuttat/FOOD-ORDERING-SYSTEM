package duckie.example.backend.mapper;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.UserRequest;
import duckie.example.backend.dto.UserResponse;
import duckie.example.backend.entity.User;

@Component
public class UserMapper {
    public User toEntity(UserRequest request) {
        if (request == null) return null;
        return User.builder()
                .fullname(request.fullname())
                .username(request.username())
                .email(request.email())
                .password(request.password()) 
                .role(duckie.example.backend.entity.Role.USER) 
                .status(duckie.example.backend.entity.UserStatus.ACTIVE)
                .build();
    }
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