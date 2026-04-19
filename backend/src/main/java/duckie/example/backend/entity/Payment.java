package duckie.example.backend.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "transaction_id")
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    public Payment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public static PaymentBuilder builder() { return new PaymentBuilder(); }

    public static final class PaymentBuilder {
        private Long id;
        private Order order;
        private String transactionId;
        private PaymentMethod method;
        private BigDecimal amount;
        private PaymentStatus status;
        private Instant createdAt;
        private Instant updatedAt;

        public PaymentBuilder id(Long id) { this.id = id; return this; }
        public PaymentBuilder order(Order order) { this.order = order; return this; }
        public PaymentBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public PaymentBuilder method(PaymentMethod method) { this.method = method; return this; }
        public PaymentBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public PaymentBuilder status(PaymentStatus status) { this.status = status; return this; }
        public PaymentBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public PaymentBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public Payment build() {
            Payment p = new Payment();
            p.setId(this.id);
            p.setOrder(this.order);
            p.setTransactionId(this.transactionId);
            p.setMethod(this.method);
            p.setAmount(this.amount);
            p.setStatus(this.status);
            p.setCreatedAt(this.createdAt);
            p.setUpdatedAt(this.updatedAt);
            return p;
        }
    }
}