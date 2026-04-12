package duckie.example.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duckie.example.backend.dto.PaymentResponse;
import duckie.example.backend.entity.Payment;
import duckie.example.backend.entity.PaymentStatus;
import duckie.example.backend.mapper.PaymentMapper;
import duckie.example.backend.repository.PaymentRepository;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Transactional
    public PaymentResponse processMockPayment(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin thanh toán cho đơn hàng này!"));
        payment.setStatus(PaymentStatus.SUCCESS);
        payment = paymentRepository.save(payment);
        
        return paymentMapper.toResponse(payment);
    }
    
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentHistory(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin thanh toán!"));
        return paymentMapper.toResponse(payment);
    }
}