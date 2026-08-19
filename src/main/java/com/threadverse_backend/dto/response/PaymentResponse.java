package com.threadverse_backend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long orderId;

    private Long paymentId;

    private String paymentMethod;

    private String paymentStatus;

    private BigDecimal amount;

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private String razorpayKeyId;

    private String currency;
}