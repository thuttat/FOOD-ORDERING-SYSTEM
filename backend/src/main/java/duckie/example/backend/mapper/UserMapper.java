package duckie.example.backend.mapper;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.UserPatchRequest;
import duckie.example.backend.dto.UserRequest;
import duckie.example.backend.dto.UserResponse;
import duckie.example.backend.entity.Role;
import duckie.example.backend.entity.User;
import duckie.example.backend.entity.UserStatus;

@Component
public class UserMapper {
    public User toEntity(UserRequest request) {
        if (request == null) return null;
        return User.builder()
                .fullname(request.fullname())
                .username(request.username())
                .email(request.email())
                .phone(request.phone()) 
                .password(request.password()) 
                .role(Role.USER) 
                .status(UserStatus.ACTIVE)
                .build();
    }

    public UserResponse toResponse(User user){
        if(user == null) return null;
        return new UserResponse(
            user.getId(),
            user.getFullname(),
            user.getUsername(),
            user.getEmail(),
            user.getPhone(), 
            user.getRole(),
            user.getStatus(),
            user.getCreatedAt(),
            user.getUpdatedAt() 
        );
    }

    public void partialUpdate(User user, UserPatchRequest request) {
        if (request == null) return;
        if (request.fullname() != null) user.setFullname(request.fullname());
        if (request.phone() != null) user.setPhone(request.phone());
        if (request.email() != null) user.setEmail(request.email());
        if (request.username() != null) user.setUsername(request.username());
    }
}