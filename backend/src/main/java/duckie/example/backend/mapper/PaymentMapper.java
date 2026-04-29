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
        if (request == null || order == null) {
            return null;
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(request.method());
        payment.setAmount(order.getTotalAmount());

        payment.setTransactionId(transactionId);
        payment.setStatus(PaymentStatus.PENDING);

        return payment;
    }

    public PaymentResponse toResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getTransactionId(),
                payment.getMethod() != null ? payment.getMethod().name() : null,
                payment.getAmount(),
                payment.getStatus() != null ? payment.getStatus().name() : null,
                "",
                payment.getCreatedAt()
        );
    }
}