package duckie.example.backend.dto;

import duckie.example.backend.entity.Role;
import duckie.example.backend.entity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserPatchRequest(
    @Size(min = 3, max = 50, message = "Username phải từ 3 đến 50 ký tự")
    String username,

    @Email(message = "Email không hợp lệ")
    String email,
    
    @Size(min = 6, max = 100,message = "Password must be between 6 and 100 characters")
    String password,

    @Deprecated
    Role role,
    @Deprecated
    UserStatus status


) {}

