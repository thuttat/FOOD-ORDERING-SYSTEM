package duckie.example.backend.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
    Long id,
    Long orderId,
    String transactionId,
    String method,
    BigDecimal amount,
    String status,
    String paymentUrl, 
    Instant createdAt
) {}