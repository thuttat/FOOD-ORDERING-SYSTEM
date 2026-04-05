package duckie.example.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderRequest(
    @NotNull(message = "Mã nhà hàng không được để trống")
    Long restaurantId,

    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    String deliveryAddress,

    String customerNote,

    @NotEmpty(message = "Đơn hàng phải có ít nhất một món ăn")
    @Valid 
    List<OrderItemRequest> items
) {
}