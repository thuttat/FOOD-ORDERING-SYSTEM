package duckie.example.backend.dto;

import duckie.example.backend.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "Fullname không được để trống")
    @Size(min = 3, max = 50)
    String fullname,

    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50)
    String username,

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email,
    
    @NotBlank(message = "Password can not be blank")
    @Size(min = 6, max = 100,message = "Password must be between 6 and 100 characters")
    String password,

    @Deprecated
    Role role
) {
    
}
