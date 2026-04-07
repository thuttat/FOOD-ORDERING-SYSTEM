package duckie.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RestaurantRequest(
    @NotBlank(message = "Tên nhà hàng không được để trống")
    @Size(min = 3, max = 200, message = "Tên nhà hàng phải từ 3 đến 200 ký tự")
    String name,
    String phoneNumber,
    @NotBlank(message = "Địa chỉ không được để trống")
    String address,
    String description,
    String imageUrl,
    Boolean isOpen,
    long ownerId
) {
}