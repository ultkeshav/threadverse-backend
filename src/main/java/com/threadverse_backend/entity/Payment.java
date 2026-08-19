package com.threadverse_backend.entity;

import com.threadverse_backend.enums.PaymentMethod;
import com.threadverse_backend.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_id",
            nullable = false,
            unique = true
    )
    private Order order;

    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "payment_method",
            nullable = false
    )
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "payment_status",
            nullable = false
    )
    private PaymentStatus paymentStatus;

    @Column(
            name = "razorpay_order_id",
            unique = true
    )
    private String razorpayOrderId;

    @Column(
            name = "razorpay_payment_id",
            unique = true
    )
    private String razorpayPaymentId;

    @Column(
            name = "razorpay_signature"
    )
    private String razorpaySignature;
}