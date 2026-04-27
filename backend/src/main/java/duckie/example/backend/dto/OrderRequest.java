package duckie.example.backend.dto;

import duckie.example.backend.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record OrderRequest(
    @NotNull(message = "Pls fill your restaurant id")
    Long restaurantId,
    @NotBlank(message = "Pls fill your address")
    String deliveryAddress,

    String customerNote,

    @NotNull(message = "Pls fill your payment method")
    PaymentMethod paymentMethod 
) {}