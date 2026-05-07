package duckie.example.backend.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import duckie.example.backend.dto.PaymentRequest;
import duckie.example.backend.dto.PaymentResponse;
import duckie.example.backend.service.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
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
    public void vnpayCallback(
            @RequestParam("vnp_TxnRef") String txnRef,
            @RequestParam("vnp_ResponseCode") String responseCode,
            HttpServletResponse response) throws IOException {
        paymentService.processPaymentCallback(txnRef, responseCode);
      
        response.sendRedirect("http://localhost:3000/payment-result?status=" + responseCode);
    }

    @GetMapping("/momo-callback")
    public void momoCallback(
            @RequestParam("orderId") String orderId,
            @RequestParam("resultCode") Integer resultCode,
            HttpServletResponse response) throws IOException { 

        paymentService.processMomoCallback(orderId, resultCode);
        response.sendRedirect("http://localhost:3000/payment-result?status=" + resultCode);
    }
}