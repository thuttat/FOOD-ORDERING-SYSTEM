package duckie.example.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequest(
    @NotNull(message = "Mã đơn hàng không được để trống")
    Long orderId,

    @NotBlank(message = "Phương thức thanh toán không được để trống")
    String method,

    @NotNull(message = "Số tiền không được để trống")
    @Positive(message = "Số tiền không hợp lệ")
    BigDecimal amount
) {}