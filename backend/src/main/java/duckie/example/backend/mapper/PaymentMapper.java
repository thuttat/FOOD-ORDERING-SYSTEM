package duckie.example.backend.mapper;

import org.springframework.stereotype.Component;

import duckie.example.backend.dto.PaymentRequest;
import duckie.example.backend.dto.PaymentResponse;
import duckie.example.backend.entity.Order;
import duckie.example.backend.entity.Payment;
import duckie.example.backend.entity.PaymentStatus;

@Component
public class PaymentMapper {
    public Payment toEntity(PaymentRequest request, Order order, String transactionId) {
        if (request == null) return null;
        return Payment.builder()
                .order(order)
                .transactionId(transactionId)
                .method(request.method()) 
                .amount(request.amount())
                .status(PaymentStatus.PENDING)
                .build();
    }

    public PaymentResponse toResponse(Payment payment, String paymentUrl) {
        if (payment == null) return null;
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getTransactionId(),
                payment.getMethod().name(),
                payment.getAmount(),
                payment.getStatus().name(),
                paymentUrl, 
                payment.getCreatedAt()
        );
    }
}