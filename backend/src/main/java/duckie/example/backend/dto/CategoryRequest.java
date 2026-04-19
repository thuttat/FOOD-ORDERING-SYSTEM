package duckie.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
    @NotNull(message = "Id cannot be null")
    Long restaurantId,

    @NotBlank(message = "name cannot be null")
    @Size(max = 100, message = "name must be under 100 characters")
    String name
) {}