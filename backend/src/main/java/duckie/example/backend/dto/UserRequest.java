package duckie.example.backend.dto;

import duckie.example.backend.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest (
        @NotBlank(message = "Fullname can not be blank")
        @Size(min = 3, max = 100)
        String fullname,

        @NotBlank(message = "Username can not be blank")
        @Size(min = 3, max = 50)
        String username,

        @NotBlank(message = "Email can not be blank")
        @Email(message = "Email is invalid")
        String email,

        @NotBlank(message = "Phone number is required")
        String phone, 

        @NotBlank(message = "Password can not be blank")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password,

//        @Deprecated => a mún xóa dòng này để linh hoạt gán role ở bên frontend được nữa
        Role role
){}