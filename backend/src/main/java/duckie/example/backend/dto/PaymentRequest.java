package duckie.example.backend.dto;

import duckie.example.backend.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull(message = "Bill id cannot be blank")
        Long orderId,

        @NotNull(message = "Method cannot be blank")
        PaymentMethod method
) {}