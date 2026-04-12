package duckie.example.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewRequest(
    @NotNull(message = "Mã nhà hàng không được để trống")
    Long restaurantId,

    @NotNull(message = "Điểm đánh giá không được để trống")
    @Min(value = 1, message = "Điểm đánh giá thấp nhất là 1")
    @Max(value = 5, message = "Điểm đánh giá cao nhất là 5")
    Integer rating,

    String comment
) {}