package duckie.example.backend.dto;

import duckie.example.backend.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;


public record OrderRequest(
    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    String deliveryAddress,

    String customerNote,

    @NotBlank(message = "Phương thức thanh toán không được để trống")
    PaymentMethod paymentMethod 
) {}