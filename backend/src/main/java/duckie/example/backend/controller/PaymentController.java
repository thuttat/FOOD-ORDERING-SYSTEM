package duckie.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import duckie.example.backend.dto.PaymentRequest;
import duckie.example.backend.dto.PaymentResponse;
import duckie.example.backend.service.PaymentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPayment(request));
    }

    @GetMapping("/vnpay-callback")
    public ResponseEntity<String> vnpayCallback(
            @RequestParam("vnp_TxnRef") String txnRef,
            @RequestParam("vnp_ResponseCode") String responseCode) {

        paymentService.processPaymentCallback(txnRef, responseCode);
        return ResponseEntity.ok("Payment VNPAY Processed. Please return to the app.");
    }

    @GetMapping("/momo-callback")
    public ResponseEntity<String> momoCallback(
            @RequestParam("orderId") String orderId,
            @RequestParam("resultCode") Integer resultCode) {

        paymentService.processMomoCallback(orderId, resultCode);
        return ResponseEntity.ok("Payment MoMo Processed. Please return to the app.");
    }
}