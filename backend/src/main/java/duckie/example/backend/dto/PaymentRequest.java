package duckie.example.backend.dto;

import java.math.BigDecimal;

import duckie.example.backend.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequest(
    @NotNull(message = "Bill id cannot be blank")
    Long orderId,

    @NotBlank(message = "Method cannot be blank")
    PaymentMethod method,

    @NotNull(message = "Pls fill the total amount")
    @Positive(message = "the total amount doesn't match")
    BigDecimal amount
) {}