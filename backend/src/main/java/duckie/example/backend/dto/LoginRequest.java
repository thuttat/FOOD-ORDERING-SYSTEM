package duckie.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "Username or Email can not be blank")
    String usernameOrEmail,

    @NotBlank(message = "Password can not be blank")
    @Size(min = 6, max = 100,message = "Password must be between 6 and 100 characters")
    String password    
) {
    
}
