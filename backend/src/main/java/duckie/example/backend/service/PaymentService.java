package duckie.example.backend.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import duckie.example.backend.dto.PaymentRequest;
import duckie.example.backend.dto.PaymentResponse;
import duckie.example.backend.entity.Order;
import duckie.example.backend.entity.OrderStatus;
import duckie.example.backend.entity.Payment;
import duckie.example.backend.entity.PaymentMethod;
import duckie.example.backend.entity.PaymentStatus;
import duckie.example.backend.entity.User;
import duckie.example.backend.repository.CartItemRepository;
import duckie.example.backend.repository.CartRepository;
import duckie.example.backend.repository.OrderRepository;
import duckie.example.backend.repository.PaymentRepository;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final NotificationService notificationService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.payment.vnpay.tmn-code}") private String VNP_TMNCODE;
    @Value("${app.payment.vnpay.hash-secret}") private String VNP_HASHSECRET;
    @Value("${app.payment.vnpay.url}") private String VNP_URL;
    @Value("${app.payment.vnpay.return-url}") private String VNP_RETURN_URL;

    @Value("${app.payment.momo.partner-code}") private String MOMO_PARTNER_CODE;
    @Value("${app.payment.momo.access-key}") private String MOMO_ACCESS_KEY;
    @Value("${app.payment.momo.secret-key}") private String MOMO_SECRET_KEY;
    @Value("${app.payment.momo.endpoint}") private String MOMO_ENDPOINT;
    @Value("${app.payment.momo.return-url}") private String MOMO_RETURN_URL;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository,
                          CartRepository cartRepository, CartItemRepository cartItemRepository,
                          NotificationService notificationService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = paymentRepository.findByOrderId(order.getId()).orElseGet(() -> {
            Payment p = new Payment();
            p.setOrder(order);
            p.setAmount(order.getTotalAmount());
            p.setTransactionId("TXN_" + System.currentTimeMillis());
            p.setStatus(PaymentStatus.PENDING);
            return p;
        });
        payment.setMethod(request.method());

        String url = (request.method() == PaymentMethod.VNPAY) ? createVnPayUrl(order) :
                (request.method() == PaymentMethod.MOMO) ? createMomoUrl(order) : "/orders";

        logger.info("Payment URL generated [method={}]: {}", request.method(), url);

        if (request.method() == PaymentMethod.COD) {
            payment.setStatus(PaymentStatus.PENDING);
            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);
            completeOrderProcess(order.getCustomer(), order.getId());
        }
        

        paymentRepository.save(payment);
        return new PaymentResponse(payment.getId(), order.getId(), payment.getTransactionId(),
                payment.getMethod().name(), payment.getAmount(), payment.getStatus().name(), url, payment.getCreatedAt());
    }

    @Transactional
    public void processMomoCallback(String orderIdUnique, Integer resultCode) {
        Long id = Long.parseLong(orderIdUnique.split("_")[0]);
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        Payment p = paymentRepository.findByOrderId(id).orElseThrow(() -> new RuntimeException("Payment not found"));

        if (resultCode != null && resultCode == 0) {
            p.setStatus(PaymentStatus.SUCCESS);
            order.setStatus(OrderStatus.CONFIRMED);
            completeOrderProcess(order.getCustomer(), order.getId());
        } else {
            p.setStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.CANCELLED);
        }
        orderRepository.save(order);
        paymentRepository.save(p);
    }

    @Transactional
    public void processPaymentCallback(String txnRef, String status) {
        Long id = Long.parseLong(txnRef.split("_")[0]);
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        Payment p = paymentRepository.findByOrderId(id).orElseThrow(() -> new RuntimeException("Payment not found"));

        if ("00".equals(status)) {
            p.setStatus(PaymentStatus.SUCCESS);
            completeOrderProcess(order.getCustomer(), order.getId());

            order.setStatus(OrderStatus.CONFIRMED); 
            orderRepository.save(order);

        } else {
            p.setStatus(PaymentStatus.FAILED);
        }
        paymentRepository.save(p);
    }

    private void completeOrderProcess(User customer, Long orderId) {
        cartRepository.findByCustomerId(customer.getId()).ifPresent(cart -> {
            cartItemRepository.deleteByCartId(cart.getId());
        });
        notificationService.createAndSaveNotification(customer, "Order #" + orderId + " placed successfully!", "ORDER");
    }

    private String createVnPayUrl(Order order) {
        Map<String, String> vnp = new HashMap<>();
        vnp.put("vnp_Version", "2.1.0");
        vnp.put("vnp_Command", "pay");
        vnp.put("vnp_TmnCode", VNP_TMNCODE);
        vnp.put("vnp_Amount", String.valueOf(order.getTotalAmount().multiply(java.math.BigDecimal.valueOf(100)).longValue()));
        vnp.put("vnp_CurrCode", "VND");
        String uniqueRef = order.getId() + "_" + UUID.randomUUID().toString().substring(0, 8);
        vnp.put("vnp_TxnRef", uniqueRef);
        vnp.put("vnp_OrderInfo", "Pay HappyFood #" + order.getId());
        vnp.put("vnp_OrderType", "other");
        vnp.put("vnp_Locale", "vn");
        vnp.put("vnp_ReturnUrl", VNP_RETURN_URL);
        vnp.put("vnp_IpAddr", "127.0.0.1");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        vnp.put("vnp_CreateDate", formatter.format(new java.util.Date()));

        String result = buildUrl(VNP_URL, vnp, VNP_HASHSECRET, "HmacSHA512");
        logger.info("VNPay URL built: {}", result);
        return result;
    }

    private String createMomoUrl(Order order) {
        String requestId = UUID.randomUUID().toString();
        String orderIdUnique = order.getId() + "_" + System.currentTimeMillis();
        String amount = String.valueOf(order.getTotalAmount().longValue());
        String orderInfo = "pay with MoMo";
        String extraData = "";

        String rawSignature = "accessKey=" + MOMO_ACCESS_KEY +
            "&amount=" + amount +
            "&extraData=" + extraData +
            "&ipnUrl=" + MOMO_RETURN_URL +
            "&orderId=" + orderIdUnique +
            "&orderInfo=" + orderInfo +
            "&partnerCode=" + MOMO_PARTNER_CODE +
            "&redirectUrl=" + MOMO_RETURN_URL +
            "&requestId=" + requestId +
            "&requestType=captureWallet";

        String signature = hmac(MOMO_SECRET_KEY, rawSignature, "HmacSHA256").toLowerCase();

        Map<String, Object> body = new HashMap<>();
        body.put("partnerCode", MOMO_PARTNER_CODE);
        body.put("partnerName", "HappyFood");
        body.put("storeId", "HappyFoodStore");
        body.put("requestId", requestId);
        body.put("amount", amount);
        body.put("orderId", orderIdUnique);
        body.put("orderInfo", orderInfo);
        body.put("redirectUrl", MOMO_RETURN_URL);
        body.put("ipnUrl", MOMO_RETURN_URL);
        body.put("lang", "vi");
        body.put("extraData", extraData);
        body.put("requestType", "captureWallet");
        body.put("signature", signature);

        try {
            Map<?, ?> res = restTemplate.postForObject(MOMO_ENDPOINT, body, Map.class);
            if (res != null && res.containsKey("payUrl")) {
                return (String) res.get("payUrl");
            }
            logger.error("MoMo Error Response: {}", res);
            throw new RuntimeException("Momo Response Empty");
        } catch (Exception e) {
            logger.error("Momo Connection Error: {}", e.getMessage());
            throw new RuntimeException("Momo Connection Error: " + e.getMessage());
        }
    }

    private String buildUrl(String baseUrl, Map<String, String> params, String secret, String algo) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String k : keys) {
            String v = params.get(k);
            if (v != null && !v.isEmpty()) {
                String encodedKey = URLEncoder.encode(k, StandardCharsets.US_ASCII);
                String encodedValue = URLEncoder.encode(v, StandardCharsets.US_ASCII);

                hashData.append(encodedKey).append('=').append(encodedValue).append('&');
                query.append(encodedKey).append('=').append(encodedValue).append('&');
            }
        }
        if (hashData.length() > 0) hashData.setLength(hashData.length() - 1);
        if (query.length() > 0) query.setLength(query.length() - 1);

        String secureHash = hmac(secret, hashData.toString(), algo).toUpperCase();
        return baseUrl + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    private String hmac(String key, String data, String algo) {
        try {
            Mac mac = Mac.getInstance(algo);
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algo));
            byte[] res = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : res) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}